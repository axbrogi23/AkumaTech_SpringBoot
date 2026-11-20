# AkumaTech

Tienda gamer desarrollada con React para el proyecto **Fullstack II — Duoc UC**. La aplicación permite explorar un catálogo de hardware y periféricos, administrar un carrito de compras, registrarse, iniciar sesión y gestionar productos y usuarios desde un panel administrativo.

![React](https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=20232A)
![Vite](https://img.shields.io/badge/Vite-8-646CFF?logo=vite&logoColor=white)
![Vitest](https://img.shields.io/badge/Vitest-3-729B1B?logo=vitest&logoColor=white)

## Funcionalidades

- Catálogo de 14 productos gamer.
- Carrusel de productos destacados.
- Carrito con aumento, disminución, eliminación y vaciado de productos.
- Códigos de descuento y alternativas de envío.
- Registro con validación de correo, contraseña, teléfono y RUT chileno.
- Inicio y cierre de sesión mediante API.
- Historial de pedidos y opción de volver a comprar.
- Panel de administración protegido por rol.
- CRUD de productos y usuarios.
- Persistencia local de carrito, productos, usuarios, pedidos y sesión.
- Pruebas unitarias, de integración, accesibilidad y flujos de usuario.

## Tecnologías

| Área | Tecnología |
| --- | --- |
| Interfaz | React 19 |
| Build y desarrollo | Vite 8 |
| Lenguaje | JavaScript y JSX |
| Estado global | React Context |
| Persistencia | `localStorage` |
| Pruebas | Vitest, Testing Library y jest-axe |
| Estilos | CSS3, Flexbox y Grid |

## Requisitos

- Node.js 18 o superior.
- npm 9 o superior.
- Un backend disponible para las operaciones de registro e inicio de sesión.

## Instalación y ejecución

```bash
# Clonar el proyecto
git clone <URL_DEL_REPOSITORIO>
cd Akuma_tech_React-main

# Instalar dependencias
npm install

# Iniciar el servidor de desarrollo
npm run dev
```

Vite mostrará en la terminal la URL local, normalmente `http://localhost:5173`.

### Configuración del backend

El frontend utiliza por defecto la API:

```text
http://localhost:7778/api/auth
```

Para utilizar otra dirección, crea un archivo `.env.local` en la raíz del proyecto:

```env
VITE_API_URL=http://localhost:7778/api/auth
```

El backend debe exponer los siguientes endpoints:

| Método | Endpoint | Uso |
| --- | --- | --- |
| `POST` | `/register` | Registrar un usuario |
| `POST` | `/login` | Iniciar sesión y devolver un token |

El token recibido se guarda en `localStorage` con la clave `akuma_token`.

## Scripts disponibles

```bash
npm run dev             # Servidor de desarrollo con HMR
npm run build           # Compilación para producción
npm run preview         # Vista previa del build
npm run lint            # Revisión estática con ESLint
npm test                # Ejecutar todas las pruebas
npm run test:watch      # Ejecutar pruebas en modo watch
npm run test:coverage   # Generar reporte de cobertura
```

## Estructura del proyecto

```text
src/
├── assets/              # Imágenes y recursos multimedia
├── components/          # Header, Footer, Carrusel y ProductoCard
├── context/             # Estado global de sesión y carrito
├── data/                # Catálogo, descuentos y opciones de envío
├── pages/               # Vistas de tienda, autenticación y administración
├── tests/               # Pruebas automatizadas
├── utils/               # Validaciones y helpers de localStorage
├── App.jsx              # Navegación y composición principal
├── index.css            # Estilos globales
└── main.jsx             # Punto de entrada
```

La navegación se controla desde `App.jsx` mediante estado local, sin utilizar React Router. Los contextos `SesionContext` y `CarritoContext` centralizan la sesión, el catálogo y el carrito.

## Backend Spring Boot

El backend REST del proyecto se encuentra en [`backend/`](./backend). Utiliza
Spring Boot, Java, Gradle, H2, BCrypt y JWT para las operaciones de
autenticación y gestión de productos.

Para ejecutarlo se requiere JDK 17 y Gradle:

```bash
cd backend
gradle bootRun
```

La API de autenticación utiliza los siguientes endpoints:

| Método | Endpoint | Uso |
| --- | --- | --- |
| `POST` | `/api/auth/register` | Registrar un usuario |
| `POST` | `/api/auth/login` | Iniciar sesión y obtener un token |

El frontend espera que el backend esté disponible en
`http://localhost:7778/api/auth`, aunque esta dirección se puede cambiar con
`VITE_API_URL`.

## Credenciales de prueba

El panel administrativo crea un usuario administrador por defecto si todavía no existe:

| Rol | Correo | Contraseña |
| --- | --- | --- |
| Administrador | `admin@gmail.com` | `admin123` |

## Códigos de descuento

| Código | Descuento |
| --- | --- |
| `AKUMA10` | 10% |
| `GAMER30` | 30% |
| `BIENVENIDO15` | 15% |
| `DESCUENTO5` | 5% |
| `CYBERAKUMA40` | 40% |

## Documentación adicional

- [Diagrama de arquitectura](./diagrama_arquitectura.md)
- [Diagrama de flujo](./diagrama_de_flujo.md)

## Notas

- Los datos del carrito, productos, sesión y pedidos se almacenan en el navegador.
- `node_modules/`, `dist/` y `coverage/` son directorios generados y están excluidos del control de versiones.
- Las imágenes externas del catálogo requieren conexión a Internet para cargarse correctamente.
