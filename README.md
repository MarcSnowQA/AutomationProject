# SauceDemo UI Automation Project 🚀

This is a robust, end-to-end UI testing framework built specifically to automate the testing of the [SauceDemo Web Application](https://www.saucedemo.com/). It has been built to guarantee 100% adherence to defined test cases across different user flows, leveraging Page Object Model (POM) design patterns and Allure for detailed reporting.

## 🛠️ Technology Stack
- **Language**: Java 21
- **Testing Framework**: TestNG (7.3.0)
- **Web Automation**: Selenium WebDriver (4.36.0)
- **Driver Management**: WebDriverManager (6.1.0) by Bonigarcia
- **Build Tool**: Apache Maven
- **Reporting**: Allure Reporting (2.29.0)

## 📁 Framework Architecture
The framework follows the industry-standard **Page Object Model (POM)** structure.

```
src/
└── test/
    ├── java/
    │   ├── pageobjects/     # Encapsulates web elements and business logic
    │   │   ├── BasePage.java            
    │   │   ├── LoginPage.java           
    │   │   ├── InventoryPage.java       
    │   │   ├── ProductPage.java         
    │   │   ├── CartPage.java            
    │   │   ├── CheckoutStepOnePage.java 
    │   │   ├── CheckoutStepTwoPage.java 
    │   │   ├── CheckoutCompletePage.java
    │   │   └── MenuPage.java            
    │   ├── tests/           # Dedicated test classes mapping to test cases
    │   │   ├── BaseTest.java            # Setup, teardown, taking screenshots
    │   │   ├── LoginTests.java          
    │   │   ├── InventoryTests.java      
    │   │   ├── ProductTest.java         
    │   │   ├── CheckoutTests.java       
    │   │   └── MenuTests.java           
    │   └── utils/           # Helper classes and global variables
    └── resources/           
```

## 🧪 Test Coverage
The suite covers over 30 granular business test scripts grouped via XML:
1. **Login Flow**: Valid connections, invalid user blocks, empty fields matching expected error messages.
2. **Product Inventory**: Dynamically checking DOM elements counts, basket states, filtering dropdown sorting logic (low-to-high algorithm verifications).
3. **Cart & Item Views**: Ensuring navigation buttons work back and forth symmetrically.  
4. **Checkout Calculations**: Extracting string UI data into backend `double` primitives and automating calculations validating math against item properties. Negative validation handling for incomplete info loops.
5. **Session States**: Resetting app states checking global DOM impacts, validating login storage persistence.

## ⚙️ How to Run Tests

### Prerequisites
- JDK 21 installed and configured in your `$PATH`
- Maven installed
- Google Chrome browser locally installed (no strict strict version requisite due to WebDriverManager handling binary fetching).

### Execution 
Run the entire test suite via `testng.xml` implicitly using Maven:
```bash
mvn clean test
```

## 📊 Result Reporting (Allure)
This project is deeply integrated with Allure Framework. If a test fails, `BaseTest.java` ensures a real-time **Screenshot** is passed natively back into your generated graphical report payload!

To serve the visual analytics dashboard:
```bash
mvn allure:serve
```
This automatically compiles data located within `/target/allure-results/` and hosts a temporary Web Server showcasing metrics, environment properties, durations, and attached visuals on failure points.

## 🪚 Maintenance Guidelines
- To add a new UI element: Add `@FindBy` annotation to the respective class within `/pageobjects`.
- Always add Javadoc for abstract elements.
- Assertions (`Assert.assertEquals`, etc.) exist strictly in the `/tests` package, avoiding assertions in page objects.
