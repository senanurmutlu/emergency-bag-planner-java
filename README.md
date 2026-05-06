# emergency-bag-planner-java
emergency-bag-planner-java
#  Emergency Bag Planner System (Java)

##  Project Overview

This project is a Java-based console application that simulates an **emergency preparedness system** for earthquake scenarios. It helps users create personalized emergency bags based on family members, risk levels, and predefined item categories.

The system automatically loads items from files, calculates total weight, and ensures that the bag does not exceed its capacity using custom exception handling.

---

The goal of this project is to:
- Simulate real-world emergency preparedness planning
- Practice object-oriented programming (OOP) principles
- Implement exception handling in Java
- Work with file handling and data persistence
- Model real-life decision systems (risk-based item selection)
##  How It Works
1. The user creates a family profile
2. Each person is assigned a category (Adult, Baby, Elderly, Chronic Patient)
3. The system selects items based on:
   - Risk level (High / Medium / Low)
   - Person category
4. Items are loaded from text files
5. Items are added to the emergency bag
6. If capacity is exceeded, a custom exception is triggered
7. Final bag summary is displayed
##  Technologies Used
- Java
- Object-Oriented Programming (OOP)
- Exception Handling (Custom Exceptions)
- File I/O (FileReader, FileWriter, Scanner)
- Enums
- Collections (ArrayList)
- Console-based 
## Key Concepts
- Inheritance (Person → Adult, Baby, Elderly, ChronicPatient)
- Polymorphism (Category-based item loading)
- Encapsulation
- Custom Exception (OverweightBagException)
- Enum usage (RiskLevel)
## 📁 File Structure
- `adult_items.txt`
- `baby_items.txt`
- `elderly_items.txt`
- `chronic_items.txt`
- `base_items_high.txt`
- `base_items_medium.txt`
- `base_items_low.txt`
##  How to Run
### 1. Compile
````bash
javac Main.java
java project.Main
