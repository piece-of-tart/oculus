
# oculus
Telegram bot that allows you to subscribe to Internet resources and be notified when they change.
At the moment there is support for
- github.com
- stackoverflow.com.

The application consists of 3 services: link parser, telegram bot and scraper.


TODO:
-   Настроить нормальное отображение команды start при перезагрузке сервера bot
и при повторной отправке start.
- Вынести логику проверки корректности ссылки в аспект
  (сейчас проверка происходит при добавлении)