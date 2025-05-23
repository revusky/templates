package org.congocc.templates.utility;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.congocc.templates.core.Environment;
import org.congocc.templates.core.variables.UserDirectiveBody;
import org.congocc.templates.core.variables.UserDirective;

/**
 * Performs an XML escaping of a given template fragment. Specifically,
 * &lt; &gt; &quot ' and &amp; are all turned into entity references.
 *
 * <p>An instance of this tarnsform is initially visible as shared
 * variable called <tt>xml_escape</tt>.</p>
 * 
 * @version $Id: XmlEscape.java,v 1.29 2004/11/27 14:49:57 ddekany Exp $
 */
public class XmlEscape implements UserDirective {

    private static final char[] LT = "&lt;".toCharArray();
    private static final char[] GT = "&gt;".toCharArray();
    private static final char[] AMP = "&amp;".toCharArray();
    private static final char[] QUOT = "&quot;".toCharArray();
    private static final char[] APOS = "&apos;".toCharArray();

    public void execute(Environment env, Map<String, Object> args, Object[] bodyVars, UserDirectiveBody body) throws IOException {
        body.render(getWriter(env.getOut()));
    }
    
    public Writer getWriter(final Writer out)
    {

        return new Writer()
        {
            @Override
            public void write(int c) throws IOException
            {
                switch(c)
                {
                    case '<': out.write(LT, 0, 4); break;
                    case '>': out.write(GT, 0, 4); break;
                    case '&': out.write(AMP, 0, 5); break;
                    case '"': out.write(QUOT, 0, 6); break;
                    case '\'': out.write(APOS, 0, 6); break;
                    default: out.write(c);
                }
            }

            @Override
            public void write(char cbuf[], int off, int len) throws IOException
            {
                int lastoff = off;
                int lastpos = off + len;
                for (int i = off; i < lastpos; i++)
                {
                    switch (cbuf[i])
                    {
                        case '<': out.write(cbuf, lastoff, i - lastoff); out.write(LT, 0, 4); lastoff = i + 1; break;
                        case '>': out.write(cbuf, lastoff, i - lastoff); out.write(GT, 0, 4); lastoff = i + 1; break;
                        case '&': out.write(cbuf, lastoff, i - lastoff); out.write(AMP, 0, 5); lastoff = i + 1; break;
                        case '"': out.write(cbuf, lastoff, i - lastoff); out.write(QUOT, 0, 6); lastoff = i + 1; break;
                        case '\'': out.write(cbuf, lastoff, i - lastoff); out.write(APOS, 0, 6); lastoff = i + 1; break;
                    }
                }
                int remaining = lastpos - lastoff;
                if(remaining > 0)
                {
                    out.write(cbuf, lastoff, remaining);
                }
            }

            @Override
            public void flush() throws IOException {
                out.flush();
            }

            @Override
            public void close() {
            }
        };
    }
}
