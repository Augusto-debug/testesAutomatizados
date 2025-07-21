package org.iftm.gerenciadorveterinarios;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class BaseSeleniumTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    
    @LocalServerPort
    protected int port;
    
    protected String baseUrl;

    @BeforeEach
    public void setUp() {
        // Configurar WebDriverManager para gerenciar automaticamente o ChromeDriver
        WebDriverManager.chromedriver().setup();
        
        // Configurar opções do Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Executar em modo headless para CI/CD
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        // Inicializar o WebDriver
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10);
        
        // Definir URL base
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    /**
     * Navega para a página inicial da aplicação
     */
    protected void navegarParaHome() {
        driver.get(baseUrl + "/home");
    }
    
    /**
     * Navega para a página de cadastro
     */
    protected void navegarParaCadastro() {
        driver.get(baseUrl + "/form");
    }
    
    /**
     * Navega para a página de pesquisa
     */
    protected void navegarParaPesquisa() {
        driver.get(baseUrl + "/find");
    }
} 