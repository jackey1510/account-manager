# Account Manager

Account manager to get account balance and transfer money

## Environment Variables

This project is built on openjdk 11. Please set your JAVA_HOME to your JDK path, e.g.

```bash
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
```

## Run Locally

Clone the project

```bash
  git clone https://github.com/jackey1510/account-manager.git
```

Go to the project directory

```bash
  cd account-manager
```

Install dependencies

```bash
  ./gradlew clean build
```

Start the server

```bash
  ./gradlew bootRun
```

## Run with Docker

```bash
  docker compose up
```

## Running Tests

To run tests, run the following command

```bash
  ./gradlew test
```

## API Reference

see http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

## Features

- Get balance of account
- Transfer money to another account in HKD

  