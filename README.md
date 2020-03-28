# examen-mercadolibre
Ejercicio de solución al exámen de mercadolibre 2020
Servicio REST de Java con Spring Boot

## Funcionalidades

### Verificación de adn mutante 
Ante determinado ADN se verifica mediante sus secuencias si el mismo pertenece a un mutante o no. Cada verificación se guarda en la base de datos, ya sea mutante o no.

El endpoint es **POST http://{dominio}:{puerto}/mutants**, se debe enviar por body un JSON como el siguiente:

*{"dna": ["ACTACG", "CATTTT", "GTACAT", "GATAGG", "CGCCTA", "TCACTG"]}*

**Nota:** Se debe tener una cantidad de palabras N con longitud N para llevar a cabo el test de manera exitosa. Por ejemplo, 6 palabras de 6 letras cada una. Las palabras deben contener las letras **A**, **C**, **T** Y **G**.

### Estadisticas 
Se consulta la cantidad de resultados positivos para ADN mutante y humano, y la proporcion de adn mutantes / adn humano.

El endpoint es **GET http://{dominio}:{puerto}/stats**

## Cómo iniciar la aplicación (Local)

1. Clonarse el repositorio en la PC con `git clone http://github.com/acanosa/examen-mercadolibre`

2. Iniciar un servidor MySQL para la base de datos

3. Crear una base de datos con el nombre **mutantes** 

4. Asignar o crear un usuario para acceder a la base de datos.

5. Configurar la url de conexión en el archivo *application.properties* (en src/main/resources) la property `spring.datasource.url` con el siguiente formato: jdbc:mysql://{host}:{puerto}/{nombre-base}?serverTimezone=GMT-3.

Siendo:
 - host: el dominio o ip sobre la que esta hosteada la base de datos
 - puerto: el puerto donde escucha el servidor de base de datos
 - nombre-base: el nombre de la base de datos que vamos a usar para guardar los datos

*Ejemplo para una base "mutantes" en localhost: jdbc:mysql://localhost:3306/mutantes?serverTimezone=GMT-3*

6. Configurar el usuario en la property `spring.datasource.username`

7. Configurar el nombre del schema para flyway (el mismo para la base de datos que se configuro en la base 5)

8. Compilar la aplicación para instalar las dependencias del POM

9. Iniciar el servidor de base de datos

10. Iniciar la aplicación y verificar que se crearon las tablas `flyway-schema-history` y `mutante`

11. Con Postman, realizar las peticiones que se describen en la sección **Funcionalidades**, ver a continuación la sección de **casos de prueba**

## Casos de prueba

### Casos exitosos

Para el caso exitoso puede haber 3 patrones en las palabras, siendo necesarios 2 o más para indicar que el adn es mutante, los patrones son los siguientes: 
a. **4 letras iguales juntas** en una palabra 
b. Observando las palabras como una tabla de N filas, **4 letras iguales en vertical** (o en la misma posicion en cada palabra) 
c. Imaginando la tabla con las palabras como en el punto anterior, **4 letras en diagonal en ambos sentidos**, es decir, moviendose de a 1 lugar 4 veces, cada vez en una palabra diferente

- Para un caso donde solo podamos encontrar menos de 2 patrones, devuelve **`false`** con el status code **403 FORBIDDEN**

- Para el caso **a**: {"dna": ["AAAACG", "CATTTT", "TTTCGT", "GCTTGG", "CGCCTA", "TCACTG"]}. El servidor debe devolver **`true`** con status code **200 OK**.
Notese que hay dos palabras tienen las caracteristicas descritas. Si solo hubiese una y no hubiera más secuencias que respeten los puntos **a**, **b** o **c** el servidor devuelve **`false`** como se explico en el punto anterior

- Para el caso **b**: {"dna": ["ACGTCG", "ACTGTA", "ACACGT", "ACTAGG", "CCCCTA", "TCACTG"]}. El servidor debe devolver **`true`** con un status code **200 OK**
Notese que en este caso hay 4 cadenas que empiezan con la letra **A** y luego hay una cadena con cuatro letras **C** juntas

- Para el caso **c**: {"dna": ["ACGTCG", "CATGTA", "GCACGT", "CCTAGG", "CCCCTA", "TCACTG"]}. El servidor debe devolver **`true`** con un status code **200 OK**.
Notese que en este caso la primer palabra tiene una **A** en el lugar 1, la segunda otra en el lugar 2, y asi hasta la 4 inclusive; y además un patrón de 4 letras iguales en la penúltima palabra.



### Casos inválidos

- En el caso donde se envie al menos una letra que no sea valida, el servidor devuelve un mensaje de error con el status **400 BAD REQUEST** sin importar que tipo de adn es
- Para el caso donde se envien palabras que no respeten la longitud indicada (su longitud es diferente a la cantidad de palabras) devuelve un mensaje de error con el status **400 BAD REQUEST**
- Para un caso donde no se envía una lista o esta esta vacía, devuelve un mensaje de error con el status code **400 BAD REQUEST**
