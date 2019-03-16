[![Build Status](https://travis-ci.org/vermucht/zip-archiver.svg?branch=master)](https://travis-ci.org/vermucht/zip-archiver)
[![codecov](https://codecov.io/gh/vermucht/zip-archiver/branch/master/graph/badge.svg)](https://codecov.io/gh/vermucht/zip-archiver)

<h1>Консольный zip-архиватор</h1>

<h4>Что требовалось</h4>
<ol>
    <li>Заданием было создать консольное приложение для создания zip-архива из файловой структуры.</li>
    <li>В параметрах указывается:
        <ul>
            <li> Корневой котолог, где находятся нужные файлы и папки..</li>
            <li> Расширения файлов, которые должны попасть в архив.</li>
        </ul>
    <li>Целью задания было научиться работать с java.util.ZipOutputStream, а также научиться фильтровать файлы по их имени (расширению).</li>
    <li>В процессе я увлекся и сделал не только архиватор, но и разархиватор</li> 
</ol>

<h4>Как собрать</h4>
<ol>
    <li>Для сборки потребуется 
    <a href="https://maven.apache.org/download.cgi">
    Maven</a>, для запуска - 
    <a href="https://jdk.java.net/11/">
        OpenJDK 11</a>.
    <li>
        <a href="https://github.com/vermucht/zip-archiver/archive/master.zip">
        Скачать</a> 
        исходный код с github и распаковать (либо клонировать репозиторий).
    </li>
    <li>Перейти в папку проекта (`zip-archiver`).</li>
    <li>Запустить сборку при помощи Maven:  
        
```
mvn clean package 
```

</li>
    <li>Перейти в папку `target` и убедиться, что там есть файл `zip-archiver.jar`.
    <li>Далее предполагается, что все операции выполняются в папке, где находится файл `zip-archiver.jar`.</li>
</ol>

<h4>Примеры работы</h4>
<h5>Архивация</h5>