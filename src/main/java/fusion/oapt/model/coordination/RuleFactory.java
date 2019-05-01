
package fusion.oapt.model.coordination;

import fusion.oapt.model.coordination.rule.AllDifferent;
import fusion.oapt.model.coordination.rule.Annotation;
import fusion.oapt.model.coordination.rule.EquivalentClass;
import fusion.oapt.model.coordination.rule.EquivalentProperty;
import fusion.oapt.model.coordination.rule.IntersectionOf;
import fusion.oapt.model.coordination.rule.List;
import fusion.oapt.model.coordination.rule.OntologyHeader;
import fusion.oapt.model.coordination.rule.Redefinition;
import fusion.oapt.model.coordination.rule.SameAs;
import fusion.oapt.model.coordination.rule.UnionOf;
import fusion.oapt.model.coordination.Coordinator;

public class RuleFactory
{
    public static Coordinator getAnnotationRule()
    {
        return new Annotation();
    }

    public static Coordinator getDeprecatedRule()
    {
        return new fusion.oapt.model.coordination.rule.Deprecated();
    }

    public static Coordinator getListRule()
    {
        return new List();
    }

    public static Coordinator getOntologyHeaderRule()
    {
        return new OntologyHeader();
    }

    public static Coordinator getIntersectionOfRule()
    {
        return new IntersectionOf();
    }

    public static Coordinator getAllDifferentRule()
    {
        return new AllDifferent();
    }

    public static Coordinator getUnionOfRule()
    {
        return new UnionOf();
    }

    public static Coordinator getRedefinitionRule()
    {
        return new Redefinition();
    }

    public static Coordinator getEquivalentClassRule()
    {
        return new EquivalentClass();
    }

    public static Coordinator getEquivalentPropertyRule()
    {
        return new EquivalentProperty();
    }

    public static Coordinator getSameAsRule()
    {
        return new SameAs();
    }
}
