package io.apptik.tarator.yogurt;


public abstract class AbstractInteraction<AC extends AbstractAction<AC, AS, I, A>, AS extends AbstractAssert<AC, AS, I, A>, I extends AbstractInteraction<AC, AS, I, A>, A> implements Interaction<AC, AS, I, A> {


    protected final I myself;
    protected final A actual;
    protected final AC action;
    protected final AS assertion;


    protected AbstractInteraction(A actual, Class<?> selfType) {
        myself = (I) selfType.cast(this);
        this.actual = actual;
        action = getAction(actual);
        assertion = getAssertion(actual);
    }

    protected abstract AC getAction(A actual);
    protected abstract AS getAssertion(A actual);


    @Override
    public AS assertThat() {
        return assertion;
    }

    @Override
    public AC perform() {
        return action;
    }


}
