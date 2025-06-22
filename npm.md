# Javascript using NodeJS and NPM

## NPM

### Cleaning

`npm cache clean --force`

### info about packages

`npm list -g`  
`npm list -g --depth=0`  
`npm view <package> version`  
`npm info <package> version`

### Memory increasing while running

`--max-old-space-size=8192` could be set as environment variable `NODE_OPTIONS` to control node memory. Or passing it to `NodeJs` directly. Example in NPM scripts

```json
"scripts": {
    "start": "cross-env NODE_OPTIONS=--max_old_space_size=4096 webpack"
}
```

Note that it's important to specify the option with_underscores since that's the only one that NODE_OPTIONS accepts.

## PNPM, NestJs & NextJs

## Commands

```sh
# node_modules/.bin is added to the PATH, so pnpm exec allows executing commands of dependencies
pnpm exec jest

# Fetches a package from the registry without
# installing it as a dependency, hotloads it, and runs
# whatever default command binary it exposes
pnpm dlx create-vue my-app
pnpx create-vue my-app

# Add package globally
pnpm add -g @nestjs/cli
```

### NestJs new project

```sh
# Modify main.ts as instructed
# https://docs.nestjs.com/techniques/performance#adapter
nest new --strict -l TS -p pnpm <project_name>
cd <project_name>
pnpm add @nestjs/platform-fastify
pnpm rm @nestjs/platform-express
```

`main.ts` content referenced from [this link](https://docs.nestjs.com/techniques/performance#performance-fastify)

```Typescript
import { NestFactory } from '@nestjs/core';
import {
  FastifyAdapter,
  NestFastifyApplication,
} from '@nestjs/platform-fastify';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create<NestFastifyApplication>(
    AppModule,
    new FastifyAdapter()
  );
  await app.listen(process.env.PORT ?? 3000, '0.0.0.0');
}
bootstrap();
```

```sh
pnpm run start -b swc
pnpm run start:dev -b swc
pnpm run lint
pnpm run format 
```

### NextJs new project

```sh
# https://nextjs.org/docs/app/api-reference/cli/create-next-app
pnpm dlx create-next-app@latest dynamic-forms-client  --app --ts --tailwind --eslint --src-dir --turbopack --use-pnpm
cd dynamic-formst-client

# https://tailwindcss.com/docs/installation/framework-guides/nextjs
pnpm add tailwindcss @tailwindcss/postcss postcss

# https://ui.shadcn.com/docs/installation/manual
pnpm dlx shadcn@latest init -t next -b neutral --src-dir --css-variables
pnpm dlx shadcn@latest add button

# Themes
# https://ui.shadcn.com/docs/dark-mode/next
pnpm add next-themes

# Development
pnpm run dev
```

- Edit `globals.css` for prefix configurations of tailwind `@import "tailwindcss" prefix(tw);`.
- Edit prefix config in `components.json`
- add `"baseUrl": ".",` to `tsconfig.json` under `compilerOptions` object.
- modify `package.json` to have the script `"dev": "next dev --turbopack",`
- Debugging Reference in IDE or browser
  - <https://nextjs.org/docs/app/guides/debugging>.
  - <https://nodejs.org/en/learn/getting-started/debugging>
