tasks {
    task("Task_1_1_1", "Heap sort", 1.0)
    task("Task_1_1_2", "String finder", 1.0)
    task("Task_1_2_1", "Stack", 1.0)
    task("Task_1_3_1", "Tree", 1.0)
    task("Task_1_3_2", "Grade Book", 1.0)
    task("Task_1_4_1", "My First Calculator", 1.0)
    task("Task_1_4_2", "Notebook", 1.0)
    task("Task_2_1_1", "Parallel primes", 1.0)
    task("Task_2_2_1", "Pizzeria", 1.0)
    task("Task_2_3_1", "Snake", 1.0)
}

group("20213") {
    student("leadp", "Ilya Merzlyakov", "https://github.com/leadpogrommer/OOP.git", "master") {
        useRootProject()
    }
    student("vlada", "Vlada Arkhipova", "https://github.com/vlada967/OOP", "main") {
        useRootProject()
    }
    student("alina", "Alina Guselnikova", "https://github.com/alinaguselnikova/OOP", "master")
    student("mzolot", "Mila Zolotareva", "https://github.com/MZolot/OOP", "main") {
        useRootProject()
    }
    student("korotkov", "Nikita Korotkov", "https://github.com/n-korotkov/OOP", "main")
    student("ramil", "Ramil Salakhov", "https://github.com/ymimsr/OOP", "master") {
        useRootProject()
        taskBranch("Task_2_1_1", "lab_2_1_1")
    }
    student("burgher", "Egor Maximov", "https://github.com/BurgheRlyeh/OOP", "master") {
        useRootProject()
    }
}

defaultAssignments {
    assign("Task_1_1_1", "2021-10-01")
    assign("Task_1_1_2", "2021-10-15")
    assign("Task_1_2_1", "2021-10-29")
    assign("Task_1_3_1", "2021-12-28")
    assign("Task_1_3_2", "2021-11-12")
    assign("Task_1_4_1", "2021-11-26")
    assign("Task_1_4_2", "2021-12-10")
    assign("Task_2_1_1", "2022-03-16")
    assign("Task_2_2_1", "2022-03-30")
    assign("Task_2_3_1", "2022-05-25")
}

javaHome("/usr/lib/jvm/java-11-openjdk/")
timeout(20000)