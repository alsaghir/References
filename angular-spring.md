# Angular

```powershell

$projectName = 'new-project'

# Install yarn globally
npm install --global yarn

# Install angular cli globally using yarn
yarn global add @angular/cli

# Add the output to the path
yarn global bin

# Add yarn to be used by angular globally
ng config -g cli.packageManager yarn

# Create new project
ng new $projectName
cd $projectName

# Add yarn to be used by angular locally
ng config cli.packageManager yarn

yarn add bootstrap

# Make ./src is the base file path
$json = Get-Content '.\tsconfig.json' | Out-String | ConvertFrom-Json -Depth 100
$json.compilerOptions.baseUrl = './src'
$json | ConvertTo-Json -depth 100 | Out-File -FilePath '.\tsconfig.json'

# move to styles folder
New-Item -Type Directory .\src\styles
Move-Item -Path .\src\styles.scss -Destination .\src\styles\styles.scss
New-Item -Type File .\src\styles\styles-dark.scss

# Configure angular.json
$json = Get-Content '.\angular.json' | Out-String | ConvertFrom-Json -Depth 100
# bootstrap js with popper
$json.projects.$projectName.architect.build.options.scripts = @('./node_modules/bootstrap/dist/js/bootstrap.bundle.min.js')
# Style config
$stylesJson = @"
[
  {
    "input": "src/styles/styles.scss",
    "bundleName": "app"
  },
  {
   "input": "src/styles/styles-dark.scss",
    "bundleName": "dark",
    "inject": false
 }
]
"@
$json.projects.$projectName.architect.build.options.styles = ($stylesJson | ConvertFrom-Json -Depth 100)
# Write config
$json | ConvertTo-Json -depth 100 | Out-File -FilePath '.\angular.json'

# Create initial bootstrap style config
New-Item -Type Directory .\src\styles\vendor
New-Item -Type File .\src\styles\vendor\_bootstrap-functions.scss
New-Item -Type File .\src\styles\vendor\_bootstrap.scss

$boostrapFunctions = @"
/* Take a look to the following for better idea
~bootstrap/scss/bootstrap.scss
https://getbootstrap.com/docs/5.1/customize/sass/
*/

// 1. Include functions first (so you can manipulate colors, SVGs, calc, etc)
@import '~bootstrap/scss/functions';

// 2. Include any default variable overrides here
"@

$bootstrapVendor = @"
/* Take a look to the following for better idea
~bootstrap/scss/bootstrap.scss
https://getbootstrap.com/docs/5.1/customize/sass/
*/

// 2. Include any default variable overrides here

// 3. Include remainder of required Bootstrap stylesheets
@import '~bootstrap/scss/variables';
@import '~bootstrap/scss/mixins';
@import '~bootstrap/scss/utilities';
@import '~bootstrap/scss/root';

// 4. Include any optional Bootstrap CSS as needed
@import '~bootstrap/scss/reboot';
@import '~bootstrap/scss/type';
@import '~bootstrap/scss/images';
@import '~bootstrap/scss/containers';
@import '~bootstrap/scss/grid';

// Helpers
@import '~bootstrap/scss/helpers';

// 5. Optionally include utilities API last to generate classes based on the Sass map in `_utililies.scss`
@import '~bootstrap/scss/utilities/api';
"@

$bootstrapVendor | Out-File -FilePath '.\src\styles\vendor\_bootstrap.scss'
$boostrapFunctions | Out-File -FilePath '.\src\styles\vendor\_bootstrap-functions.scss'
New-Item -Type Directory .\src\styles\themes
New-Item -Type File .\src\styles\themes\dark.scss
New-Item -Type Directory .\src\styles\themes\dark
New-Item -Type File .\src\styles\themes\dark\_custom.scss
New-Item -Type File .\src\styles\themes\dark\_overrides.scss
New-Item -Type File '.\src\styles\themes\default.scss'
"@import '~bootstrap/scss/bootstrap';" | Out-File -FilePath '.\src\styles\themes\default.scss'
"@charset ""UTF-8"";`n `n@import 'themes/default';" | Out-File -FilePath '.\src\styles\styles.scss'

"@charset ""UTF-8"";`n `n@import 'themes/dark';" | Out-File -FilePath '.\src\styles\styles-dark.scss'
$darkTheme = @"
@import '../vendor/bootstrap-functions';

@import './dark/overrides';

@import '../vendor/bootstrap';

@import './dark/custom';
"@
$customDark = @'
$web-font-path: "https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" !default;
@if $web-font-path {
  @import url($web-font-path);
}
'@
$overridesDark = @'
// Get overrides from Bootswatch for example
'@
$darkTheme | Out-File -FilePath '.\src\styles\themes\dark.scss'
$customDark | Out-File -FilePath '.\src\styles\themes\dark\_custom.scss'
$overridesDark | Out-File -FilePath '.\src\styles\themes\dark\_overrides.scss'
```

## tsconfig.json

```json
{
  "compileOnSave": false,
  "compilerOptions": {
    "baseUrl": "./src"
  }
}
```

# Spring

```powershell
# To be added later
```
