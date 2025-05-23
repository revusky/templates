package org.congocc.templates.builtins;

import java.util.Iterator;
import java.util.function.Function;

import org.congocc.templates.core.Environment;
import org.congocc.templates.core.nodes.generated.BuiltInExpression;
import org.congocc.templates.core.nodes.generated.TemplateNode;
import org.congocc.templates.TemplateSequenceModel;

/**
 * @author Attila Szegedi
 * @version $Id: $
 */
public class SequenceContainsBuiltIn extends ExpressionEvaluatingBuiltIn {

    @Override
    public Object get(Environment env, BuiltInExpression caller,
            Object model) 
    {
        if (!(model instanceof TemplateSequenceModel || model instanceof Iterable)) {
            throw TemplateNode.invalidTypeException(model, caller.getTarget(), env, "sequence or collection");
        }
        return new SequenceContainsFunction(model);
    }

    static class SequenceContainsFunction implements Function<Object, Boolean> {
        final Iterable collection;
        SequenceContainsFunction(Object seqModel) {
            if (seqModel instanceof Iterable) {
                collection = (Iterable) seqModel;
            }
            else {
                throw new AssertionError();
            }
        }

        public Boolean apply(Object arg) {
            Object compareToThis = arg;
            final DefaultComparator modelComparator = new DefaultComparator(Environment.getCurrentEnvironment());
            Iterator<Object> it = collection.iterator();
            while (it.hasNext()) {
                if (modelComparator.areEqual(it.next(), compareToThis)) {
                    return true;
                }
            }
            return false;
        }
    }
}
