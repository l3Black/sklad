Сервеная часть приложения по учету товаров на складе
-
API дает возможность просмотра, создания, редактирования
и удаления товаров и складов на которых эти товары хранятся,
а также ипорта документов согласно которым происходит поступление,
продажа и перемещение товара по складам. Документы сохраняются в базу данных для
просмотра истории движения товаров, но только в случае успешного применения
документа (товара достаточно на складе и артикул товара, указанного в 
документе, присутствеут в базе данных). Так же имеется возможность 
просмотра общего списка товаров по всем складам(артикул, наименование,
цены закупки и продажи) с возможностью фильтрации по имени товара
и остатка товаров (артикул, наименование, остаток по всем складам)
с возможностью фильтрации по складу.

Сборка проекта сделана максимально простой, достаточно скачать zip-архив распаковать и применить 
mvn package. После запуска jar файла стартует локальный сервер на порту 4567 или 
указанном в первом параметре при запуске.

Основные использованные фреймворки SparkJava Framework, Hibernate, Gson, JUnit.
