import nu.xom.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author ZhangGJ
 * @Date 2019/10/03
 */
class Person {
    private String first, last, address, city, state;
    private int zipCode;

    public Person(String first, String last, String address, String city, String state,
        int zipCode) {
        this.first = first;
        this.last = last;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    // Produce an XML Element from this Person object:
    public Element getXML() {
        Element person = new Element("person");
        Element firstName = new Element("section_01");
        firstName.appendChild(first);
        Element lastName = new Element("last");
        lastName.appendChild(last);
        Element addr = new Element("address");
        addr.appendChild(address);
        Element cty = new Element("city");
        cty.appendChild(city);
        Element st = new Element("state");
        st.appendChild(state);
        Element zc = new Element("zipCode");
        zc.appendChild(Integer.toString(zipCode));
        person.appendChild(firstName);
        person.appendChild(lastName);
        person.appendChild(addr);
        person.appendChild(cty);
        person.appendChild(st);
        person.appendChild(zc);
        return person;
    }

    // Constructor to restore a Person from an XML Element:
    public Person(Element person) {
        first = person.getFirstChildElement("section_01").getValue();
        last = person.getFirstChildElement("last").getValue();
        address = person.getFirstChildElement("address").getValue();
        city = person.getFirstChildElement("city").getValue();
        state = person.getFirstChildElement("state").getValue();
        zipCode = Integer.valueOf(person.getFirstChildElement("zipCode").getValue());
    }

    public String toString() {
        return first + " " + last + " " + address + " " + city + " " + state + " " + zipCode;
    }

    // Make it human-readable:
    public static void format(OutputStream os, Document doc) throws Exception {
        Serializer serializer = new Serializer(os, "ISO-8859-1");
        serializer.setIndent(4);
        serializer.setMaxLength(60);
        serializer.write(doc);
        serializer.flush();
    }
}


class People extends ArrayList<Person> {
    public People(String fileName) throws Exception {
        Document doc = new Builder().build(fileName);
        Elements elements = doc.getRootElement().getChildElements();
        for (int i = 0; i < elements.size(); i++)
            add(new Person(elements.get(i)));
    }
}


public class E31_PeopleWithAddresses {
    public static void main(String[] args) throws Exception {
        List<Person> people = Arrays
            .asList(new Person("Dr. Bunsen", "Honeydew", "Street 1", "New York", "NY", 10001),
                new Person("Gonzo", "The Great", "Street 2", "New York", "NY", 20002),
                new Person("Phillip J.", "Fry", "Street 3", "New York", "NY", 30003));
        System.out.println(people);
        Element root = new Element("people");
        for (Person p : people)
            root.appendChild(p.getXML());
        Document doc = new Document(root);
        Person.format(System.out, doc);
        Person.format(new BufferedOutputStream(new FileOutputStream("People.xml")), doc);
        People p =
            new People("/Users/zhanggengjia/Downloads/IntellijProject/thinking-in-java/People.xml");
        System.out.println(p);
    }
}
