package io.apptik.tarator.yogurt;


public interface Assert<AC extends Action<AC, AS, I, A>, AS extends Assert<AC, AS, I, A>, I extends Interaction<AC, AS, I, A>, A> extends Interaction<AC, AS, I, A> {


}
