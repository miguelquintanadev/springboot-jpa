package com.miguel.springboot.jpa.springbootjpa;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.miguel.springboot.jpa.springbootjpa.entities.Person;
import com.miguel.springboot.jpa.springbootjpa.repositories.PersonRepository;

@SpringBootApplication
public class SpringbootJpaApplication implements CommandLineRunner{

	@Autowired
	private PersonRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// findOne();
		// create();
		// update();
		// delete();
		// delete2();
		// personalizedQueries();
		personalizedQueries2();
	}

	@Transactional(readOnly = true)
	public void personalizedQueries2(){
		System.out.println("====== CONSULTA POR OBJETO PERSONA Y LENGUAJE DE PROGRAMACION ======");
		List<Object[]> personsRegs = repository.findAllMixPerson();

		personsRegs.forEach(reg ->{
			System.out.println("programmingLanguage = " + reg[1] + ", person = " + reg[0]);
		});

		System.out.println("====== CONSULTA QUE PUEBLA Y DEVUELVE OBJETO ENTITY DE UNA INSTANCIA PERSONALIZADA ======");
		List<Person> persons = repository.findAllObjectPersonalizedPerson();
		persons.forEach(System.out::println);
	}

	@Transactional(readOnly = true)
	public void personalizedQueries(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("====== CONSULTA SOLO EL NOMBRE POR ID ======");
		System.out.println("Ingrese el ID para el nombre:");
		Long id = scanner.nextLong();
		scanner.close();

		System.out.println("====== MOSTRANDO SOLO EL NOMBRE POR EL ID ======");
		String name = repository.getNameById(id);
		System.out.println(name);

		System.out.println("====== MOSTRANDO SOLO EL ID ======");
		Long idDb = repository.getIdById(id);
		System.out.println(idDb);

		System.out.println("====== MOSTRANDO NOMBRE COMPLETO CONCAT ======");
		String fullname = repository.getFullNameById(id);
		System.out.println(fullname);

		System.out.println("====== CONSULTA POR CAMPOS PERSONALIZADOS POR EL ID ======");
		Optional<Object> optionalReg = repository.obtenerPersonDataById(id);
		if(optionalReg.isPresent()){
			Object[] personReg = (Object[]) optionalReg.orElseThrow();
			System.out.println("id = "+personReg[0] + ", nombre = " + personReg[1] + ", apellido = "+ personReg[2] + ", lenguaje = "+ personReg[3]);
		}
		
		System.out.println("====== CONSULTA POR CAMPOS PERSONALIZADOS LISTA ======");
		List<Object[]> regs = repository.obtenerPersonDataList();
		regs.forEach(reg -> System.out.println("id = " + reg[0] + ", nombre = " + reg[1] + ", apellido = "+ reg[2] + ", lenguaje = "+ reg[3]));


	}

	@Transactional
	public void delete2(){
		repository.findAll().forEach(System.out::println);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID a eliminar:");
		Long id = scanner.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);

		optionalPerson.ifPresentOrElse(repository::delete,
		()-> System.out.println("No existe la persona con ese ID"));

		repository.findAll().forEach(System.out::println);
		scanner.close();
	}

	@Transactional
	public void delete(){
		repository.findAll().forEach(System.out::println);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID a eliminar:");
		Long id = scanner.nextLong();
		repository.deleteById(id);
		repository.findAll().forEach(System.out::println);
		scanner.close();
	}

	@Transactional
	public void update(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID de la persona:");
		Long id = scanner.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);

		// optionalPerson.ifPresent(person -> {
			if(optionalPerson.isPresent()){
				Person personDb = optionalPerson.orElseThrow();
				System.out.println(personDb);
				System.out.println("Ingrese el lenguaje de programación:");
				String programmingLanguage = scanner.next();
				personDb.setProgrammingLanguage(programmingLanguage);
				Person personUpdate = repository.save(personDb);
				System.out.println(personUpdate);
			}else{
				System.out.println("El ID de usuario no existe.");
			}
			
		// });

		scanner.close();
	}

	@Transactional
	public void create(){
		Scanner scanner = new Scanner (System.in);
		System.out.println("Ingrese el nombre:");
		String name = scanner.next();
		System.out.println("Ingrese el apellido:");
		String lastname = scanner.next();
		System.out.println("Ingrese el lenguaje de programación:");
		String programmingLanguage = scanner.next();
		scanner.close();

		Person person = new Person(null, name, lastname, programmingLanguage);

		Person personNew = repository.save(person);
		System.out.println(personNew);

		repository.findById(personNew.getId()).ifPresent(System.out::println);

	}

	@Transactional(readOnly = true)
	public void findOne(){
		
		// Person person = null;
		// Optional<Person> optionalPerson = repository.findById(1L);
		// if(optionalPerson.isPresent()){
		// 	person = optionalPerson.get();
		// }
		// System.out.println(person);

		repository.findOneLikeName("se").ifPresent(System.out::println);
		
	}

	@Transactional(readOnly = true)
	public void list(){
		//List<Person> persons = (List<Person>) repository.findAll();
		List<Person> persons = (List<Person>) repository.findByProgrammingLanguageAndName("Java", "Andres");

		persons.stream().forEach(person -> { 
			System.out.println(person);
		});

		List<Object[]> personsValues = repository.obtenerPersonDataByProgrammingLanguage("Java");
		personsValues.stream().forEach(person ->{
			System.out.println(person[0] + " es experto en " + person[1]);
		});
	}

}
