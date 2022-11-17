## **User Story.**

**Пользователь создает напоминания.**

Я, как пользователь, хочу создавать напоминания, чтобы освободить голову для текущих дел.

Основной сценарий:
1. Пользователь открывает телеграм бота
2. Пользователь вводит "10m впустить кота с балкона"
3. через 10 минут бот напоминает пользователю "впустить кота с балкона"

Альтернативный сценарий:
Пользователь вводит сообщение без указания таймера напоминания "впустить кота с балкона" и получает в ответ ошибку "не удалось понять сообщение"

___
**Пользователь получает напоминание через конкретный промежуток времени.**

Я, как пользователь, хочу получать напоминания, чтобы делать дела вовремя.

Основной сценарий:
1. Система достаточно часто проверяет, для каких напоминаний пришло время.
2. Система посылает наступившее напоминание пользователю, который его создал.
3. Система помечает отправленное напоминание как "обработанное" чтобы не отправлять его повторно.

Альтернативный сценарий:
Для пользователя нет новых напоминаний, ему не отправляется никаких сообщений.

___
**Пользователь может получать список напоминаний. (HTTP API)**

Я, как пользователь, хочу иметь возможность получить список своих будущих напоминаний.

Основной сценарий:
1. Пользователь вызывает метод сервиса "получить напоминания", передав свой идентификатор.
2. Сервис возвращает список напоминаний, принадлежащих этому пользователю.

Альтернативный сценарий:
Пользователя не существует, сервис возвращает пустой список.
Для пользователя нет будущих напоминаний, сервис возвращает пустой список.

___
**Пользователь имеет возможность удалять неактуальные напоминания. (HTTP API)**

Я, как пользователь, хочу иметь возможность удалить неактуальные напоминания, чтобы не получать о них уведомление.

Основной сценарий:
1. Пользователь вызывает метод сервиса "удалить напоминание", передав указатель на неактуальное напоминание.
2. Сервис удаляет напоминание и возвращает уведомление о том что напоминание успешно удалено
