package kotlindemo
//demoClasses.kt

import kotlinx.coroutines.runBlocking

object SingleCounter {
    private var counter = 0
    fun value(): Int { return counter }
    fun inc() { counter++ }
    fun dec() { counter-- }
    fun reset() { counter = 0 }
}

class Person(val name: String) {
    var age : Int = 0     //public
    var married = false   //public
    val isAdult: Boolean
    get(){ return age >= 18} //custom getter
}

data class PersonData(val name: String) {
    var age : Int = 0     //public
    var married = false   //public
    val isAdult: Boolean
    get(){ return age >= 18} //custom getter
}


class PersonCO private constructor( val name: String ){
    var age : Int = 0
    var married = false
    val isAdult: Boolean
        get(){ return age >= 18} //custom getter
  
	companion object {
	val personList = mutableListOf<PersonCO>()
	fun createPerson( name: String ) : PersonCO {
		val p = PersonCO( name.toUpperCase() )
		personList.add(p)
		return p
	}
	fun showAllPersons(){
	personList.forEach {
		println( "name=${it.name}, age=${it.age},married=${it.married} ") }
	}
  }//companion
	
  object Info {
        fun showAllAdults(){
//            val p = Person("aa")
            personList.forEach {
                if( it.isAdult )
                println( "ADULT ${it.name} of age=${it.age}  ") }
        }
        fun showOrderedByName(){
            println( personList.sortedWith( NameComparator ) )
        }

    }//Info
  object NameComparator : Comparator<PersonCO>{
        override fun compare(p1:PersonCO, p2:PersonCO):Int =
            p1.name.compareTo(p2.name)
    }//NameComparator
}//PersonCO

fun p2( c:SingleCounter ) : Int { 
	return c.value()*c.value() }

fun testObject(){
	println("------ testObject ")
    val c = SingleCounter
    val d = SingleCounter
    for( i in 1..3 ) c.inc()
    val v = p2( SingleCounter )
    println("testObject | c=${c.value()} d=${d.value()} obj=${SingleCounter.value()} v=$v")
    SingleCounter.reset()
    println("testObject | c=${c.value()} d=${d.value()} obj=${SingleCounter.value()}")	
}

fun testClass(){
	println("------ testClass ")
    val p1 = Person("Bob")		//no new
    p1.age=20
    println( "name=${p1.name}, age=${p1.age}, "+
  	  " married=${p1.married} adult=${p1.isAdult} ")
    val p2 = Person("Alice")	//no new
    p2.age = 15
    println( "name=${p2.name}, age=${p2.age}, " +
	" married=${p2.married} adult=${p2.isAdult} ")
    val p3 = Person("Bob")		//no new
    p3.age= p1.age
    println( "equals:  ${p1.equals(p3)}" )	//false
    println( "==:      ${p1 == p3}" )	//false
    println( "===:     ${p1 === p3}" )	//false
}

fun testDataClass(){
    val p1 = PersonData("Bob")
    p1.age=20
    val p3 = PersonData("Bob")
    p3.age = p1.age
    println( "p1=${p1}, p3=$p3 ")		//toString generated
    println( "equals:  ${p1.equals(p3)}" )
}


fun testCompanion(){
   //val p = PersonCO("Bob") //ERROR: init is private in PersonCO
    val p1 = PersonCO.createPerson("Bob")
    p1.age=20
    val p2 = PersonCO.createPerson("Alice")
    p2.age = 15
    PersonCO.createPerson("Adam")	//age=0
    PersonCO.showAllPersons()
    PersonCO.Info.showAllAdults()	
}

fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	
//    println( "work done in time= ${measureTimeMillis(  { testObject() } )}"  )
//    println( "work done in time= ${measureTimeMillis(  { testClass() } )}"  )
//    println( "work done in time= ${measureTimeMillis(  { testDataClass() } )}"  )
    println( "work done in time= ${measureTimeMillis(  { testCompanion() } )}"  )
	
    println("ENDS ${curThread()}")
}