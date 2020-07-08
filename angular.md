# Agnular-CLI

## Update global angular

`npm uninstall -g angular-cli`  
`npm install -g @angular/cli@latest`

## Serve from git bash on Windows

`MSYS2_ARG_CONV_EXCL="--base-href" ng build --prod --aot --base-href=/arrow-client/`

## Common Commands

`ng xi18n --output-path src/locale` Generate locale file

`ng serve --configuration=ar-EG --base-href /ar-EG/` Run with specific configuration

`MSYS2_ARG_CONV_EXCL="--base-href" ng serve --configuration=ar-EG --base-href=/ar-EG/` Run with specific configuration from git bash

`ng update`
