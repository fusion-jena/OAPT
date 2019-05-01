
package fusion.oapt.algorithm.matcher;

import fusion.oapt.general.output.Alignment;

public interface AbstractMatcher
{
    public void match();

    public Alignment getAlignment();

    public Alignment getClassAlignment();

    public Alignment getPropertyAlignment();

    public Alignment getInstanceAlignment();
}
