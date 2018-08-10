package org.etri.slice.tools.adl.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.etri.slice.tools.adl.domainmodel.Action;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Behavior;
import org.etri.slice.tools.adl.domainmodel.Call;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.generator.BehaviorGenerator;
import org.etri.slice.tools.adl.generator.IGeneratorForMultiInput;
import org.etri.slice.tools.adl.generator.OutputPathUtils;
import org.etri.slice.tools.adl.generator.compiler.LogbackCompiler;
import org.etri.slice.tools.adl.generator.compiler.MetaDataCompiler;
import org.etri.slice.tools.adl.generator.compiler.POMCompiler;

@SuppressWarnings("all")
public class DeviceProjectGenerator implements IGeneratorForMultiInput {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  @Extension
  private BehaviorGenerator _behaviorGenerator;
  
  @Inject
  @Extension
  private MetaDataCompiler _metaDataCompiler;
  
  @Inject
  @Extension
  private POMCompiler _pOMCompiler;
  
  @Inject
  @Extension
  private LogbackCompiler _logbackCompiler;
  
  @Inject
  @Extension
  private OutputPathUtils _outputPathUtils;
  
  @Override
  public void doGenerate(final List<Resource> resources, final IFileSystemAccess fsa) {
    final Function1<Resource, Iterable<AgentDeclaration>> _function = (Resource it) -> {
      return Iterables.<AgentDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), AgentDeclaration.class);
    };
    Iterable<AgentDeclaration> _flatten = Iterables.<AgentDeclaration>concat(ListExtensions.<Resource, Iterable<AgentDeclaration>>map(resources, _function));
    for (final AgentDeclaration e : _flatten) {
      {
        this.generateDevice(e, fsa);
        this.generateMavenProject(e, fsa);
        this.generateMetaData(e, fsa);
        this.generateLogback(e, fsa);
      }
    }
  }
  
  public void generateDevice(final AgentDeclaration it, final IFileSystemAccess fsa) {
    EList<Behavior> _behaviors = it.getBehaviorSet().getBehaviors();
    for (final Behavior b : _behaviors) {
      {
        EList<JvmTypeReference> _types = b.getSituation().getTypes();
        for (final JvmTypeReference t : _types) {
          {
            JvmTypeReference type = t;
            if ((type instanceof Context)) {
              this._behaviorGenerator.generateSensor(((Context)type), it, fsa);
            }
          }
        }
        Action action = b.getAction();
        if ((action instanceof Call)) {
          this._behaviorGenerator.generateService(((Call)action).getControl(), it, fsa);
        }
      }
    }
  }
  
  public void generateMavenProject(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _deviceMavenHome = this._outputPathUtils.getDeviceMavenHome(it);
    String _plus = (_deviceMavenHome + "bundle.properties");
    fsa.generateFile(_plus, this.compileBundleProperties(it));
    String _deviceMavenHome_1 = this._outputPathUtils.getDeviceMavenHome(it);
    String _plus_1 = (_deviceMavenHome_1 + "pom.xml");
    fsa.generateFile(_plus_1, this._pOMCompiler.compileDevicePOM(it));
  }
  
  public void generateMetaData(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _deviceMavenResHome = this._outputPathUtils.getDeviceMavenResHome(it);
    String _plus = (_deviceMavenResHome + "metadata.xml");
    fsa.generateFile(_plus, this._metaDataCompiler.compileMetaData(it));
  }
  
  public void generateLogback(final AgentDeclaration it, final IFileSystemAccess fsa) {
    String _deviceMavenResHome = this._outputPathUtils.getDeviceMavenResHome(it);
    String _plus = (_deviceMavenResHome + "logback.xml");
    fsa.generateFile(_plus, this._logbackCompiler.compileLogback(it));
  }
  
  public CharSequence compileBundleProperties(final AgentDeclaration it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("# Configure the created bundle");
    _builder.newLine();
    _builder.append("embed.dependency=;scope=!provided|test");
    _builder.newLine();
    _builder.append("embed.directory=lib");
    _builder.newLine();
    _builder.append("bundle.classpath=.,{maven-dependencies}\t\t");
    _builder.newLine();
    _builder.append("private.packages=org.etri.slice.devices.");
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(it.eContainer());
    _builder.append(_fullyQualifiedName);
    _builder.append(".");
    String _lowerCase = it.getName().toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(".*");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
