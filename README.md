# IncomeBalanceApp
Handle your wallet

[![CircleCI](https://circleci.com/gh/Americo91/IncomeBalanceApp.svg?style=svg&circle-token=666c64a692691c34e0ead246897d9250187c14de)](https://circleci.com/gh/Americo91/IncomeBalanceApp)

[![codecov](https://codecov.io/gh/Americo91/IncomeBalanceApp/branch/main/graph/badge.svg)](https://codecov.io/gh/Americo91/IncomeBalanceApp)

When updating the real DB you should:
- comment out @Componenet in Bootstrap.java
- modify properties spring.jpa.hibernate.ddl-auto to update
- modify properties spring.datasource.url to  jdbc:h2:money:./test;