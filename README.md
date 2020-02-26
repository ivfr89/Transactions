# Transactions

Arquitectura Clean usando como patrón de presentación MVVM a través de un único estado


## Estructura del proyecto


Es una arquitectura clean, siguiendo un patrón MVVM de un único estado.

Gradle se estructura:

-A nivel de proyecto: Aquí se definen las bibliotecas y cada una de las versiones
-A nivel de módulo: Se utilizan alias para implementar cada una de estas versiones

Se separa el concepto de versión / implementación


El código se divide en varios módulos:

app : Módulo principal de la aplicación, representa la capa mas externa incluyendo las vistas, viewmodel e implementaciones concretas del framework. Incluye el resto de módulos mencionados abajo

usecases: Casos de uso del app. Solo hay uno en este caso GetTransactions, que separa la capa de la lógica de dominio del flujo de vistas del app

data : Capa de datos, formada por el TransactionRepository y las interfaces de los datasources y red.

domain: Lógica del dominio; clases Transaction, Either y Failure

testShared: Mock común para testing

Los estados asociados a cada una de las pantallas se representan mediante un NombreActividadScreenState, y representa el estado de la pantalla en ese momento:


```
sealed class MainUIState
{
    object ShowLoader: MainUIState()
    class ShowError(val failure: Failure): MainUIState()
    class ShowTransactions(val list: List<UITransaction>): MainUIState()

}
```

Así se puede mostrar un Loader, un error que pueda ocurrir y las transacciones.
Los errores posibles pueden ser:

Failure.NetworkConnection ->  Error de conexión, se mostrará un mensaje de que no se puede obtener los datos si se recuperan los
datos de remoto sin pasar por almacenamiento local

Failure.ServerErrorCode -> Código de error que puede devolver el servidor en caso de que el servicio falle, se muestra un mensaje

Failure.ServerException -> Excepción lanzada por un fallo producido a la hora de recibir el cuerpo del mensaje del servidor, se muestra un mensaje

Failure.JsonException -> Error de parseo en el Json, se muestra un mensaje de error


En el proyecto se usa jetpack para los ViewModels, y se utilizan corrutinas de kotlin para la programación multihilo.

Este consumo de APIs se hace a través del patrón repository TransactionRepository. Lleva un manejador de conexiones para verificar que existe conexión y se envían los datos asociados y las llamadas correspondientes definidas en ApiService
Se hace uso de dos datasources: 

- ILocalDataSource que implementa CacheDataSource. Si no se fuerza el refresco, será utilizado para obtener cada una de las transacciones de forma local
- IRemoteDataSource que implementa RetrofitDataSource. Realiza la petición mediante retrofit y almacena los datos en CacheDataSource

Si no hay conexión pero hay datos en local, no se devuelve error, se muestran los datos locales




Las respuestas llevan un Either como devolución en las llamadas: Ejemplo


```
class RequestImplementation : IRequest{
        override fun <T, R> request(
            call: Call<T>,
            transform: (T) -> R,
            default: T
        ): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> {
                        Either.Right(transform((response.body() ?: default)))
                    }
                    false -> {
                        Either.Left(Failure.ServerErrorCode(response.code()))
                    }
                }
            } catch (exception: Throwable) {
                Either.Left(Failure.ServerException(exception))
            }
        }

    }
```

Either es un monad, unión disjuntiva, devuelve siempre un valor, o bien la clase izquierda (en cuyo caso será un fallo) o bien la derecha (entonces será el dato que buscas). 
Esto es así ya que de esta forma se controla cada uno de los fallos posibles a la hora de devolver la petición; la estructura permite un modelo abierto de códigos de error de petición o extender de la clase abstracta FeatureFailure para decidir qué casos de uso tienen que errores.

Luego cada capa tiene un modelo diferente: Transaction tiene: un modelo de UI donde se hacen algunas conversiones con el tiempo , un modelo del servidor entity y un modelo de dominio

## Bibliotecas más importantes

retrofit: Para el consumo del Api
koin: Service Locator para proveer dependencias de forma sencilla
viewmodel : Componente de la biblioteca de arquitectura de Android para definir ViewModels
coroutines: Multihilo
junit: Testing
mockito: Mocks para testing

## Aspectos a mejorar

- Mejorar interfaz, me he centrado en el código no en el diseño
- Terminar el testing (viewmodel e integración). Actualmente están desarrollados los test del repository y caso de uso
