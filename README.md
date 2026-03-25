# SauceDemo UI Automation Framework (saucedemo-ui-automation)

This repository contains a complete UI automation framework for testing the e‑commerce web
application [saucedemo.com](https://www.saucedemo.com).
It is built with Java 21, Selenium WebDriver (Selenium Manager), TestNG, and Allure Reports, following modern Page
Object Model (POM), component design, and parallel execution best practices.
---

## Table of Contents

- [Overview](#overview)
- [Key Technologies and Tools](#key-technologies-and-tools)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Configuration](#configuration)
- [Test Coverage](#test-coverage)
- [Reports](#reports)
- [CI/GitHub Actions](#ci/github-actions)

---

## Overview

This project demonstrates a comprehensive automation setup designed for:

High readability and maintainability
parallel execution
reporting with Allure (including screenshots for passed/failed tests)
Cross-browser support via Selenium Manager
Synthetic test data generation using DataFaker
---

## Key Technologies and Tools

- **Java 21**
- **Selenium Manager**
- **TestNG**
- **Allure Reporting**
- **Maven**
- **Lombok**
- **SLF4J + Logback**
- **Retry Analyzer**
- **Screenshot**
- **Parallel execution**
- **GitHub Actions CI**
- **DataFaker 2.x**

---

## Architecture

The framework is structured as follows:

- **src/main/java**: Contains the main codebase, including page objects, components, utilities, and configuration
  classes.
- **src/test/java**: Contains test classes organized by feature or functionality.
- **src/test/resources**: Contains test data, configuration files, and Allure properties.
- **pom.xml**: Maven configuration file with dependencies and build settings.
- **allure-results**: Directory where Allure stores test execution results.
- **allure-report**: Directory where Allure generates the HTML report.
- **README.md**: This documentation file.
- **.github/workflows**: Contains GitHub Actions workflow files for CI/CD.
- **.gitignore**: Specifies files and directories to be ignored by Git.

---

## Prerequisites

- Java 21 JDK installed and configured in your system's PATH.
- Maven installed and configured in your system's PATH.
- Internet connection for downloading dependencies and accessing the saucedemo website.
- (Optional) An IDE such as IntelliJ IDEA or Eclipse for easier code navigation and execution.

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/ChandikaH/saucedemo-ui-automation.git
   cd saucedemo-ui-automation
    ```
2. Install dependencies using Maven:
   ```bash
   mvn clean install
   ```
4. Run tests using Maven:
   ```bash
   mvn clean test
   ```
6. Generate Allure report:
   ```bash
    mvn allure:report
    ```
8. Open the Allure report in your browser:
   ```bash
    mvn allure:serve
   ```

---

## Running Tests

Tests can be executed in parallel across multiple browsers using TestNG's XML configuration. To run tests in parallel,
use the following command:

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

This will execute tests defined in the `testng.xml` file.

Run with a specific environment:

```bash
mvn test -Denv=qa
```

Change browser via config.json:

```json
{
  "browser": {
    "type": "firefox",
    "headless": true
  }
}
```

---

## Configuration

The framework uses a `config.json` file located in `src/test/resources` to manage configuration settings such as browser
type, base URL, and other parameters. You can modify this file to change the test execution settings without altering
the codebase.

Key settings include:

Base URL
Browser type + headless
Timeout settings
Screenshot behavior
Allure results path
Parallel thread count
Retry attempts

Example `config.json`:

```json
{
  "environment": {
    "name": "qa",
    "baseUrl": "https://www.saucedemo.com/"
  },
  "browser": {
    "type": "chrome",
    "headless": false
  },
  "timeouts": {
    "explicit": 15,
    "pageLoad": 20
  },
  "screenshot": {
    "enabled": true,
    "onFailure": true,
    "format": "png",
    "path": "target/screenshots"
  },
  "allure": {
    "enabled": true,
    "resultsPath": "target/allure-results"
  },
  "retry": {
    "enabled": true,
    "maxAttempts": 1
  },
  "execution": {
    "parallel": true,
    "threadCount": 3
  },
  "testdata": {
    "username": "",
    "password": ""
  }
}
```

---

## Test Coverage

The test suite covers a wide range of functionalities on the saucedemo website, including:

- User login and authentication
- Product listing and details
- Shopping cart operations
- Checkout process
- Cross-browser compatibility
- Data-driven testing with synthetic data generation

---

## Reports

Allure Reports are generated after test execution, providing detailed insights into test results, including:

- Test case status (passed, failed, skipped)
- Execution time
- Screenshots for passed and failed tests
- Logs and error messages
- Test case history and trends

After running tests:

```bash
mvn allure:serve
```

OR:

```bash
allure serve target/allure-results
```

This launches an interactive web server displaying:

* Steps
* Attachments
* Screenshots (pass & fail)
* DOM snapshots
* Parallel execution timeline
* Categories and history

Allure HTML reports are also generated automatically in:
target/site/allure-maven-plugin/
---

## CI/GitHub Actions

The framework is integrated with GitHub Actions for continuous integration. The workflow is defined in
`.github/workflows/saucedemo-tests.yml`, which automatically runs the test suite on every push or pull request to the
repository. The workflow includes steps for setting up the environment, installing dependencies, running tests, and
generating Allure reports. You can view the workflow results and generated reports directly in the GitHub Actions tab of
the repository.
---

## Conclusion

This UI automation framework for saucedemo.com is designed to be robust, maintainable, and scalable, following industry
best practices. It provides comprehensive test coverage and detailed reporting.