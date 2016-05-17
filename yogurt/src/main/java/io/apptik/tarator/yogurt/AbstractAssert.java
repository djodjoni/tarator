package io.apptik.tarator.yogurt;



public abstract class AbstractAssert<AC extends AbstractAction<AC, AS, I, A>, AS extends AbstractAssert<AC, AS, I, A>, I extends AbstractInteraction<AC, AS, I, A>, A> implements Assert<AC, AS, I, A>  {

    protected final I interaction;
    protected final A actual;

    protected AbstractAssert(A actual, I interaction) {
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

    protected abstract org.assertj.core.api.Assert<?, A> getAsserter();
}
