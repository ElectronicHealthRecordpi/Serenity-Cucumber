# Serenity BDD + Cucumber + Selenium - Framework de Testing

Framework de automatización de pruebas **end-to-end** construido con **Serenity BDD**, **Cucumber** y **Selenium WebDriver** para testear el login (y la selección de productos) de una aplicación web.

---

## 1. Stack tecnológico

| Componente | Versión | Rol |
|---|---|---|
| Java | 23 | Lenguaje base |
| Maven | 3.9+ | Gestor de dependencias y build |
| Serenity BDD Core | 4.2.22 | Framework BDD + wrapper de Selenium |
| Serenity Cucumber | 4.2.22 | Integración Serenity ↔ Cucumber |
| Serenity JUnit | 4.2.22 | Runner JUnit para Cucumber |
| Cucumber | (transitiva) | Motor de ejecución de features Gherkin |
| JUnit | 4.13.2 | Framework de testing (runner) |
| SLF4J Simple | 2.0.17 | Logging |
| Selenium WebDriver | (transitiva desde Serenity) | Automatización del navegador |

> **Selenium NO se declara explícitamente** en el `pom.xml`. Serenity BDD lo trae como dependencia transitiva. Esto es por diseño: **nunca instancias `WebDriver` directamente**, lo hace Serenity por ti.

---

## 2. Arquitectura del proyecto (Screenplay / Page Object híbrido)

El proyecto sigue el patrón **Page Object Model (POM)** combinado con el enfoque BDD de Cucumber. La estructura es:

```
src/test/
├── java/com/swag/pe/
│   ├── Runner.java                       # Punto de entrada
│   ├── definitions/                      # Glue code de Cucumber (Step Definitions)
│   │   └── LoginDef.java
│   ├── steps/                            # Step Libraries (lógica intermedia)
│   │   ├── login/LoginStep.java
│   │   └── validation/ValidationStep.java
│   ├── pages/                            # Page Objects (mapeo de UI)
│   │   ├── login/LoginPage.java
│   │   ├── product/SelectProductsPage.java
│   │   └── validations/ValidationPage.java
│   └── utilities/website/WebSite.java    # Util: navegación
└── resources/features/                   # Features Gherkin
    ├── 1-login.feature
    └── 2-products.feature
```

### Capas y flujo de datos

```
┌────────────────────────┐
│  .feature (Gherkin)    │  ← Lenguaje natural (Given/When/Then)
└──────────┬─────────────┘
           │ matchea por texto
           ▼
┌────────────────────────┐
│  definitions/*Def.java │  ← Step Definitions de Cucumber
│  (LoginDef)            │  ← orquestador del escenario
└──────────┬─────────────┘
           │ usa
           ▼
┌────────────────────────┐
│  steps/*Step.java      │  ← Step Libraries con @Step
│  (LoginStep,           │  ← métodos de negocio reutilizables
│   ValidationStep)      │
└──────────┬─────────────┘
           │ hereda de
           ▼
┌────────────────────────┐
│  pages/*Page.java      │  ← Page Objects (PageObject de Serenity)
│  (LoginPage,           │  ← @FindBy + WebElementFacade
│   ValidationPage)      │
└──────────┬─────────────┘
           │ usa
           ▼
┌────────────────────────┐
│  WebElementFacade      │  ← wrapper de Selenium de Serenity
│  (Selenium WebDriver)  │
└────────────────────────┘
```

---

## 3. ¿Cómo funciona Selenium aquí? (clave)

Aunque Selenium es el motor que **mueve el navegador**, en este framework **nunca escribes código Selenium puro**. El control lo lleva **Serenity BDD**, que lo envuelve (`WebElementFacade`).

### 3.1. Configuración del WebDriver

Archivo: `serenity.properties`

```properties
serenity.project.name=SWAG LABS
webdriver.driver=firefox          # Navegador a usar
webdriver.autodownload=true       # Descarga automática del driver (geckodriver)
serenity.browser.maximized=true   # Maximiza la ventana al iniciar
serenity.use.unique.browser=false # Comparte navegador entre escenarios (false = no comparte)
serenity.restart.browser.each.scenario=true  # Reinicia el navegador por cada escenario
```

> Con `webdriver.autodownload=true`, **no hace falta descargar geckodriver manualmente**. Serenity lo descarga solo la primera vez.

### 3.2. Inicialización del driver (automática, no la ves)

No hay un `@Before` que cree el driver. **Serenity lo hace por ti**:
- Cuando una clase extiende `PageObject`, Serenity le inyecta un `WebDriver` ya configurado según `serenity.properties`.
- Esa misma instancia de `WebDriver` se comparte entre todos los `PageObject` y `Step` de un mismo escenario (ThreadLocal).

### 3.3. Mapeo de elementos (Page Objects)

Ejemplo: `LoginPage.java`

```java
public class LoginPage extends PageObject {
    @FindBy(id="user-name")    protected WebElementFacade txt_username;
    @FindBy(id="password")     protected WebElementFacade txt_password;
    @FindBy(id="login-button") protected WebElementFacade btn_login;
    ...
}
```

- `@FindBy` viene de **Selenium** (`org.openqa.selenium.support.FindBy`).
- `WebElementFacade` es de **Serenity**: una capa encima de `WebElement` de Selenium con esperas implícitas, mejor logging, y reportes automáticos.

### 3.4. Acciones (qué hace Selenium "por debajo")

Cuando llamas:
```java
txt_username.sendKeys("admin");
```

Serenity → traduce a Selenium:
```java
driver.findElement(By.id("user-name")).sendKeys("admin");
```

Lo mismo con `click()`. Tú nunca tocas `driver`, `findElement` ni `By`. Toda esa lógica vive en `WebElementFacade`.

### 3.5. Navegación

`utilities/website/WebSite.java`:
```java
@Steps PageObject swag;

public void navigateTo(String url){
    swag.setDefaultBaseUrl(url);
    swag.open();  // → driver.get(url)
}
```

`open()` internamente es `driver.get(url)` + esperas automáticas de carga de página.

---

## 4. ¿Cómo funciona el test de login? (paso a paso)

### 4.1. El feature (Gherkin)

`src/test/resources/features/1-login.feature`:

```gherkin
@Suite @Login
Feature: CP01- Validar inicio de sesion
  Background: Validar el inicio de sesion con credenciales validas e invalidas
    Given el usuario navega al sitio web

  @ValidCredentials
  Scenario: 1 - Validar con credenciales correctas
    When ingresa credenciales validas
    Then la aplicacion deberia mostrar el modulo principal de productos

  @InvalidCredentials
  Scenario: 2 - Validar con credenciales incorrectas
    When ingresa credenciales invalidas
    Then la aplicacion deberia mostrar un mensaje de error
```

### 4.2. El Runner

`Runner.java`:

```java
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue     = "com.swag.pe.definitions",
    tags     = "@Login"
)
public class Runner { }
```

- `CucumberWithSerenity` = el runner que combina JUnit + Cucumber + Serenity.
- `features` → dónde están los `.feature`.
- `glue` → dónde están las definiciones de pasos.
- `tags = "@Login"` → filtra qué escenarios se ejecutan.

> **Nota**: tu `Runner` apunta a `glue="com.swag.pe.definitions"`, pero los pasos del feature están redactados en español. Para que matcheen, los métodos en `LoginDef` deben usar el texto en español (ver punto siguiente).

### 4.3. Step Definitions (definitions)

`LoginDef.java` une los pasos Gherkin con el código Java:

- `@Given("The user navigate to the login page")` → navega a la URL.
- `@When("the user (admin) should log in with valid credentials")` → ejecuta el login con `admin / Admin1234!`.
- `@Then("The application should show the dashboard page ")` → valida que el módulo de productos se vea.
- Existe una variante con credenciales inválidas que valida el mensaje de error.

Internamente usa la anotación `@Steps` de Serenity para inyectar:
- `WebSite url` → para abrir la URL.
- `LoginStep login` → para tipear usuario/contraseña y hacer clic.
- `ValidationStep validate` → para aserciones.

### 4.4. Steps Libraries (lógica)

- `LoginStep` extiende `LoginPage`. Sus métodos (`typeUsername`, `typePassword`, `clickLogin`) están anotados con `@Step("...")`, lo que hace que Serenity los muestre con nombre legible en el reporte.
- `ValidationStep` extiende `ValidationPage` y centraliza las validaciones (`titleIsVisible`, `errorMessageIsDisplayed`).

### 4.5. Page Objects (UI)

- `LoginPage` mapea: `#user-name`, `#password`, `#login-button`.
- `ValidationPage` mapea: `//div[@class='product_label']` (título productos) y `h3[data-test='error']` (mensaje de error de Swag Labs).
- `SelectProductsPage` mapea los botones de productos y el ícono del carrito.

---

## 5. Flujo de ejecución de un escenario (login OK)

1. **Maven Surefire** detecta `Runner*.java` (configurado en `pom.xml`).
2. JUnit lanza `Runner` con el runner `CucumberWithSerenity`.
3. Cucumber lee `1-login.feature` y filtra por `@Login`.
4. Para `Scenario: 1 - Validar con credenciales correctas`:
   - **Serenity** inicializa Firefox (geckodriver se descarga la 1ra vez).
   - **Background** → `Given el usuario navega al sitio web` → matchea con un step en `LoginDef` → llama `WebSite.navigateTo(url)` → `driver.get(url)`.
   - **When** → `LoginDef.userLoginWthValidCredentials()` → usa `LoginStep` → usa `LoginPage` → Selenium envía "admin" al input, "Admin1234!" al password, hace click en `#login-button`.
   - **Then** → `LoginDef.systemShowProductModule()` → `ValidationStep.titleIsVisible()` → `ValidationPage.lbl_product.isDisplayed()` → Selenium verifica que el elemento sea visible.
5. Serenity genera evidencia: screenshot por paso, logs, tiempo, estado.

---

## 6. Configuración Maven (pom.xml)

Plugins relevantes:

| Plugin | Función |
|---|---|
| `maven-surefire-plugin` | Ejecuta clases `Runner*.java` en la fase `test`. |
| `maven-failsafe-plugin` | Pensado para `**/test/**/*.java` (integration-test). |
| `maven-compiler-plugin` | Compila con Java 23. |
| `serenity-maven-plugin` | Genera el reporte HTML de Serenity en `post-integration-test` ejecutando `aggregate`. |

---

## 7. Cómo ejecutar

Desde la raíz del proyecto:

```bash
# Compilar y descargar dependencias
mvn clean install

# Ejecutar todos los tests filtrados por @Login
mvn clean verify

# Cambiar de navegador (ej: chrome)
mvn clean verify -Dwebdriver.driver=chrome

# Filtrar por tag específico
mvn clean verify -Dcucumber.filter.tags="@ValidCredentials"
```

---

## 8. Reportes

Al finalizar, Serenity genera un reporte HTML en:

```
target/site/serenity/index.html
```

Incluye:
- Dashboard con % de éxito.
- Desglose por feature y por escenario.
- **Captura de pantalla por cada paso**.
- Tiempos de ejecución.
- Cobertura de requerimientos (tags).

---

## 9. Resumen: el rol de Selenium en este framework

| Pregunta | Respuesta |
|---|---|
| ¿Quién maneja el navegador? | **Serenity** (que usa Selenium por dentro). |
| ¿Tú escribes código Selenium? | **No directamente**. Usas `WebElementFacade` y `PageObject`. |
| ¿Dónde se configura el navegador? | En `serenity.properties` (`webdriver.driver=firefox`). |
| ¿Cómo se mapean los elementos? | Con `@FindBy` de Selenium, pero sobre `WebElementFacade` de Serenity. |
| ¿Cómo se navega? | `PageObject.open()` (Serenity), que es `driver.get(url)` de Selenium. |
| ¿Cómo se hacen esperas? | **Implícitas y automáticas** vía Serenity (no usas `Thread.sleep` ni `WebDriverWait`). |
| ¿Quién toma screenshots? | **Serenity** automáticamente en cada paso. |

En resumen: **Selenium es el motor, Serenity es el timón, Cucumber es el copiloto, y tú defines el destino con Gherkin.**
