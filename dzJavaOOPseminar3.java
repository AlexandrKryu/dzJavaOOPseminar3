import java.util.Objects;
import java.util.Scanner;

// АБСТРАКЦИИ:

interface Document {
	public void saveAs();
}

interface Button {
	public void click();
}

interface Dialog {

	void start();

	void clickButton();
}

// ТОЧКА ВХОДА:

public class dzJavaOOPseminar3 {
	public static void main(String[] args) {
		// Экземпляр того, что будем "сохранять":
		Worker worker = new Worker("Alexandr", 38, 80000);

		// Создаём экземпляр нашей "сохранялки" в формате на выбор,
		// и передаём ему то, что надо сохранить:
		Dialog dialog = new SaveAsDialog(worker);
		// Запускаем "сохранялку", на этом этапе её цель -- выяснить у пользователя
		// в каком же формате сохранить переданное ей чудо:
		dialog.start();

		// Имитируем нажатие кнопки "Сохранить" пользоваетелем:
		dialog.clickButton();
	}

}

class Worker implements java.io.Serializable {
	private String name;
	private int age;
	private int salary;

	public Worker() {
	}

	public Worker(String name, int age, int salary) {
		this.name = name;
		this.age = age;
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return String.format("Имя: %s\nВозраст %d\nЗ/п %d", name, age, salary);
	}

}

// КОНКРЕТНОЕ:

enum DocType {
	XML,
	MD,
	JSON;
}

class SaveAsDialog implements Dialog {

	private static final Scanner scanner = new Scanner(System.in);

	private final Worker worker;
	private final Button button;

	protected Document document;

	/**
	 * Консольная диалог-сохранялка.
	 * 
	 * @param whatToSave Экземпляр сохраняемой сущности. Должен быть не null.
	 */
	public SaveAsDialog(Worker whatToSave) {
		this.worker = Objects.requireNonNull(whatToSave);
		this.button = new Button() {
			@Override
			public void click() {
				document.saveAs();
			}
		};
	}

	@Override
	public void start() {
		askFormat();
	}

	@Override
	public void clickButton() {
		if (document == null) {
			System.out.println("Ошибка: Сперва надо выбрать тип документа.");
			return;
		}

		button.click();
	}

	private void askFormat() {
		System.out.println("Сохраняемый:");
		System.out.println(worker.toString());
		System.out.println();

		System.out.print("Задайте тип документа (XML, MD, JSON): ");
		DocType docType = DocType.valueOf(scanner.nextLine().toUpperCase());
		document = selectDocument(docType);
	}

	private Document selectDocument(DocType docType) {
		return switch (docType) {
			case XML -> new XmlDocument(worker);
			case MD -> new MdDocument(worker);
			case JSON -> new JsonDocument(worker);
		};
	}
}

abstract class WorkerDocument implements Document {

	protected Worker worker;

	public WorkerDocument(Worker worker) {
		this.worker = Objects.requireNonNull(worker);
	}
}

class XmlDocument extends WorkerDocument {

	public XmlDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {
		System.out.printf("Типа вывожу в XML %d %d %s\n", worker.getAge(), worker.getSalary(), worker.getName());
	}
}

class MdDocument extends WorkerDocument {

	public MdDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {
		System.out.printf("Типа вывожу в MD %d %d %s\n", worker.getAge(), worker.getSalary(), worker.getName());
	}
}

class JsonDocument extends WorkerDocument {
	public JsonDocument(Worker worker) {
		super(worker);
	}

	@Override
	public void saveAs() {
		System.out.printf("Типа вывожу в JSON %d %d %s\n", worker.getAge(), worker.getSalary(), worker.getName());
	}
}