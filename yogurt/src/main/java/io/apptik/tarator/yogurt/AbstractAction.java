package io.apptik.tarator.yogurt;



public abstract class AbstractAction<AC extends AbstractAction<AC, AS, I, A>, AS extends AbstractAssert<AC, AS, I, A>, I extends AbstractInteraction<AC, AS, I, A>, A> implements Action<AC, AS, I, A>  {

    protected final I interaction;
    protected final A actual;

    protected AbstractAction(A actual, I interaction) {
        this.interaction =  interaction;
        this.actual =  actual;
    }

    @Override
    public AS assertThat() {
        return interaction.assertion;
    }

    @Override
    public AC perform() {
        return interaction.action;
    }
}
