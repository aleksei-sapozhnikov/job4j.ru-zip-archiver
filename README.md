[![Build Status](https://travis-ci.org/vermucht/zip-archiver.svg?branch=master)](https://travis-ci.org/vermucht/zip-archiver)
[![codecov](https://codecov.io/gh/vermucht/zip-archiver/branch/master/graph/badge.svg)](https://codecov.io/gh/vermucht/zip-archiver)

#Консольный zip-архиватор

<details>
  <summary>Задание:</summary>

1. Задана директория проекта, например: c:\project\
  
1. В качестве ключей передаются расширения файлов, которые должны попасть в архив.

1. Архив должен сохранять структуру проекта: вложенные папки и файлы в них.
    
1. Для архивации использовать класс java.util.zip.ZipOutputStream.

1. Запуск программы должен выглядеть следующим образом:
```
java-jar zip-archiver.jar -a archive -s /home/john/project -o /home/john/my_project.zip -e xml,pdf,txt
```
- <i>zip-archiver.jar :</i> скомпилированный файл приложения.
- <i>-a archive :</i> действие — архивировать (action).</li>
- <i>-s /home/john/project :</i> источник, папка с проектом (source).</li>
- <i>-o /home/john/my_project.zip :</i> результирующий файл (output).</li>
- <i>-e xml,pdf,txt :</i> Расширения файлов, которые нужно включить в проект (extensions).</li>
</details>

<details>
  <summary>Сделано:</summary>

1. Отдельный класс анализирует и проверяет переданные параметры приложения.

1. При запуске без параметров приложение выводит справку по использованию.

1. Написаны автоматические тесты с использованием JUnit.
Для сохранения модульности тестов использовалась библиотека Mockito.

1. Один из тестов вначале архивирует проект, затем разархивирует результат.
Присходит проверка - получилось ли в конце то же самое, что было изначально.

1. Структура папок для тестов создается во временной директории во время самих тестов. 
Нет нужды отправлять на Github заранее заготовленные файлы.

</details>