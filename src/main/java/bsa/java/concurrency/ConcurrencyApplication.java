package bsa.java.concurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

/*
* Привет, внимательный читатель задания домашки :). Я - мини-гайд, который подскажет тебе как стоит подходить к решению домашки.
* Возможно, задача кажется тебе очень сложной, а информации очень мало. В такие ситуации при работе с реальным проектом ты будешь попадать регулярно.
* В таком случае важно не паниковать и сразу приступать к исследованию задачи.
*
* Для начала попробуй реализовать само хеширование. Не обязательно сразу реализовывать его в этом проекте - можешь создать отдельный и поиграться с возможными библиотеками и вариантами решения задачи.
* Для реализации данного задания будет достаточно встроенной в Java библиотеки для работы с графикой - awt.
* Спустя несколько часов гугления и мучений у тебя скорее всего будет рабочий код, который умеет считать хеш для изображения. Если нет - можешь подсмотреть нашу реализацию в гисте.
*
* После этого тебе следует реализовать сохранение изображения на диск. Ты уже это делал, поэтому особых проблем возникнуть не должно.
*
* Далее необхожимо реализовать сохранение информации об изображении в персистентное хранилище. На этом этапе нужно оценить, сколько у вас осталось времени и желания дальше работать с домашкой.
*
* Если времени мало или желания нет - ваш выбор это хранение информации об изображениях в коллекции в оперативной памяти. Используйте обычный List и поиск по нему с помощью расстояния Хэмминга.
* Помните, что мы используем все 64 бита long-а, поэтому числа могут быть как положительными так и отрицательными.
*
* Если времени и желания достаточно - тогда твой выбор это база данных. Мы ограничиваем тебя в выборе СУБД, нужно использовать PostgreSQL.
* На других СУБД реализация такого запроса несколько более проста, в PostgreSQL прийдется либо писать хранимую функцию, либо колдовать с кастами и строками.
* Для реализации такого запроса тебе прийдется использовать механизм нативных запросов, после чего с помощью проекций смапить результат запроса на DTO.
*
* Если вы дошли до сюда - можете выдохнуть, сложная часть задания позади. Осталось лишь реализовать удаление изображений по id и удаление всех изображений.
* С этим ты уже сталкивался в предыдущих домашках, так что затруднений это вызвать не должно.
*
* Ты выполнил все функциональные требования к приложению. Самое время прикрутить логирование и немного порефакторить код перед отправкой домашки, если осталось время.
* Если ты дошел до сюда, то поздравляю - ты проделал огромную работу и узнали много нового в процессе :). Если осталось время до следующей лекции - самое время отдохнуть.
* */

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
public class ConcurrencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrencyApplication.class, args);
	}

}
