package io.apptik.tarator.yogurt;



public interface Interaction<AC extends Action<AC, AS, I, A>, AS extends Assert<AC, AS, I, A>, I extends Interaction<AC, AS, I, A>, A> {

    AC perform();

    AS assertThat();

}
