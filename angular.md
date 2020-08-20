# Agnular-CLI

## Update global angular

`npm uninstall -g angular-cli`  
`npm install -g @angular/cli@latest`

## Serve from git bash on Windows

`MSYS2_ARG_CONV_EXCL="--base-href" ng build --prod --aot --base-href=/arrow-client/`

## Common Commands

Generate locale file with different options

`ng xi18n --output-path src/locale`
`ng xi18n --i18n-locale fr`

Run with specific configuration

`ng serve --port 4300 --configuration=ar-EG --base-href /ar-EG/`

`MSYS2_ARG_CONV_EXCL="--base-href" ng serve --configuration=ar-EG --base-href=/ar-EG/` Run with specific configuration from git bash

`ng update`

## Add Locale Support for `ar-EG`

in `angular.json` add the following

- projects >> <project_name> >> architect >> build >> configurations

```json
"ar-EG": {
              "localize": ["ar-EG"]
            }
```

***WARNING*** replace <project_name> with your project name

- projects >> <project_name> >> architect >> serve >> configurations

```json
"ar-EG": {
              "browserTarget": "<project_name>:build:ar-EG"
            }
```

- projects >> <project_name>

```json
"i18n": {
        "sourceLocale": "en-US",
        "locales": {
          "ar-EG": "src/locale/messages.ar-EG.xlf"
        }
      }
```

- projects >> <project_name> >> architect >> build >> options

```json
"i18nMissingTranslation": "error"
```

- projects >> <project_name> >> architect >> build >> options

```json
"allowedCommonJsDependencies": ["node-forge"]
```

## TS Config

```json
{
    // Import json file
    "resolveJsonModule": true,
    "esModuleInterop": true,

    // For CommonJS libraries
    "allowSyntheticDefaultImports": true
}
```
