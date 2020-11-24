# NPM

`npm cache clean --force`

## info about packages

`npm list -g`  
`npm list -g --depth=0`  
`npm view <package> version`  
`npm info <package> version`

## Memory increasing while running

`--max-old-space-size=8192` could be set as environment variable `NODE_OPTIONS` to control node memory. Or passing it to `NodeJs` directly. Example in NPM scripts

```json
"scripts": {
    "start": "cross-env NODE_OPTIONS=--max_old_space_size=4096 webpack"
}
```

Note that it's important to specify the option with_underscores since that's the only one that NODE_OPTIONS accepts.
