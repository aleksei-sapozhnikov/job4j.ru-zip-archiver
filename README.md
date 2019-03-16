[![Build Status](https://travis-ci.org/vermucht/zip-archiver.svg?branch=master)](https://travis-ci.org/vermucht/zip-archiver)
[![codecov](https://codecov.io/gh/vermucht/zip-archiver/branch/master/graph/badge.svg)](https://codecov.io/gh/vermucht/zip-archiver)

# Консольный zip-архиватор

##### Техзадание

1. Создать консольное приложение для создания zip-архива из файловой структуры.

1. Требуемые опции: 
    - Корневой котолог, где находятся нужные файлы и папки.
    - Расширения файлов, которые должны попасть в архив.
        
1. Целью задания было научиться работать с `java.util.ZipOutputStream`, а также научиться фильтровать файлы по их имени (расширению).
1. В процессе я увлекся и сделал не только архиватор, но и разархиватор 

##### Как собрать

1. Для сборки потребуется [Maven](https://maven.apache.org/download.cgi), 
для запуска — [OpenJDK 11](https://jdk.java.net/11/).

1. [Скачать](https://github.com/vermucht/zip-archiver/archive/master.zip)
архив с исходным кодом из репозитория и распаковать 
(вместо этого можно просто клонировать репозиторий).

1. Перейти в папку проекта (`zip-archiver`). 
Запустить сборку при помощи Maven: 
    ```
    mvn clean package
    ```

1. Перейти в папку `target` и убедиться, что в ней появился файл `zip-archiver.jar`.
Далее предполагается, что все операции выполняются в папке, где находится файл `zip-archiver.jar`.