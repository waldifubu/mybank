package alittlexample;

class Test {
    public static void main(String[] args) {
        //Comparable x = new SomeClass();
        //x.compareTo(x);


        String str = "abc";
        Object obj = new Object();
        obj = str;

        MyClass<String> myClass1 = new MyClass<String>();
        MyClass<Object> myClass2 = new MyClass<Object>();
        obj = myClass1;

        //myClass1 = myClass2;

    }


   static class SomeClass implements Comparable {
        public int compareTo(Object other) {
            return 0;
        }
    }
    public static class MyClass<T> {
    }

}
