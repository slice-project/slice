package org.etri.slice.tools.adl.generator.compiler;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;

@SuppressWarnings("all")
public class MetaDataCompiler {
  public CharSequence compileMetaData(final AgentDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<ipojo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("xsi:schemaLocation=\"org.apache.felix.ipojo http://felix.apache.org/ipojo/schemas/CURRENT/core.xsd\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("xmlns=\"org.apache.felix.ipojo\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<!--");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("Declare your component types and instances here");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("-->");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<!--");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<component classname=\"$YOUR_COMPONENT_CLASS\">");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</component>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<instance component=\"$YOUR_COMPONENT_CLASS\" />");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("-->");
    _builder.newLine();
    _builder.append("</ipojo>\t\t");
    _builder.newLine();
    return _builder;
  }
}
