package org.etri.slice.tools.adl.validation.domain_dependency;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.etri.slice.tools.adl.domainmodel.Action;
import org.etri.slice.tools.adl.domainmodel.AgentDeclaration;
import org.etri.slice.tools.adl.domainmodel.Behavior;
import org.etri.slice.tools.adl.domainmodel.Call;
import org.etri.slice.tools.adl.domainmodel.Command;
import org.etri.slice.tools.adl.domainmodel.CommandSet;
import org.etri.slice.tools.adl.domainmodel.Context;
import org.etri.slice.tools.adl.domainmodel.Control;
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;
import org.etri.slice.tools.adl.domainmodel.Event;
import org.etri.slice.tools.adl.domainmodel.Publish;
import org.etri.slice.tools.adl.validation.domain_dependency.DomainManager;

@SuppressWarnings("all")
public class DomainDependencyUtil {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  private DomainManager domainManager;
  
  public DomainManager checkDomainDependencies(final List<Resource> resources) {
    DomainManager _xblockexpression = null;
    {
      this.domainManager.init();
      final Function1<Resource, Iterable<DomainDeclaration>> _function = (Resource it) -> {
        return Iterables.<DomainDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), DomainDeclaration.class);
      };
      Iterable<DomainDeclaration> _flatten = Iterables.<DomainDeclaration>concat(ListExtensions.<Resource, Iterable<DomainDeclaration>>map(resources, _function));
      for (final DomainDeclaration domain : _flatten) {
        this.domainManager.addDomain(domain);
      }
      final Function1<Resource, Iterable<DomainDeclaration>> _function_1 = (Resource it) -> {
        return Iterables.<DomainDeclaration>filter(IteratorExtensions.<EObject>toIterable(it.getAllContents()), DomainDeclaration.class);
      };
      Iterable<DomainDeclaration> _flatten_1 = Iterables.<DomainDeclaration>concat(ListExtensions.<Resource, Iterable<DomainDeclaration>>map(resources, _function_1));
      for (final DomainDeclaration domain_1 : _flatten_1) {
        this.traveseDomain(domain_1);
      }
      _xblockexpression = this.domainManager;
    }
    return _xblockexpression;
  }
  
  public String getDomain(final String packageName, final String prefix, final String suffix) {
    String _xblockexpression = null;
    {
      final String tmp = packageName.replace(prefix, "");
      String _xifexpression = null;
      if ((suffix != null)) {
        int _lastIndexOf = tmp.lastIndexOf(suffix);
        int _minus = (_lastIndexOf - 1);
        _xifexpression = tmp.substring(1, _minus);
      } else {
        _xifexpression = tmp.substring(1);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String getDomain(final String qName, final String suffix) {
    String _xifexpression = null;
    if ((suffix != null)) {
      int _lastIndexOf = qName.lastIndexOf(suffix);
      int _minus = (_lastIndexOf - 1);
      _xifexpression = qName.substring(1, _minus);
    } else {
      _xifexpression = qName.substring(1);
    }
    return _xifexpression;
  }
  
  public void traveseDomain(final DomainDeclaration domain) {
    final EList<EObject> eContents = domain.eContents();
    final String domainFQN = this._iQualifiedNameProvider.getFullyQualifiedName(domain).toString();
    Iterable<Context> _filter = Iterables.<Context>filter(eContents, Context.class);
    for (final Context context : _filter) {
      JvmParameterizedTypeReference _superType = context.getSuperType();
      boolean _tripleNotEquals = (null != _superType);
      if (_tripleNotEquals) {
        final String qualifiedName = context.getSuperType().getQualifiedName();
        boolean _equals = qualifiedName.equals("void");
        if (_equals) {
          return;
        }
        final String package_ = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
        final String depDomain = this.getDomain(package_, "org.etri.slice.commons", "context");
        boolean _equals_1 = domainFQN.equals(depDomain);
        boolean _not = (!_equals_1);
        if (_not) {
          this.domainManager.addRef(domain, domainFQN, depDomain);
        }
      }
    }
    Iterable<Control> _filter_1 = Iterables.<Control>filter(eContents, Control.class);
    for (final Control control : _filter_1) {
      final Consumer<JvmParameterizedTypeReference> _function = (JvmParameterizedTypeReference it) -> {
        final String qualifiedName_1 = it.getQualifiedName();
        boolean _equals_2 = qualifiedName_1.equals("void");
        if (_equals_2) {
          return;
        }
        final String package__1 = qualifiedName_1.substring(0, qualifiedName_1.lastIndexOf("."));
        final String depDomain_1 = this.getDomain(package__1, "org.etri.slice.commons", "service");
        boolean _equals_3 = domainFQN.equals(depDomain_1);
        boolean _not_1 = (!_equals_3);
        if (_not_1) {
          this.domainManager.addRef(domain, domainFQN, depDomain_1);
        }
      };
      control.getSuperTypes().forEach(_function);
    }
    Iterable<Event> _filter_2 = Iterables.<Event>filter(eContents, Event.class);
    for (final Event event : _filter_2) {
      JvmParameterizedTypeReference _superType_1 = event.getSuperType();
      boolean _tripleNotEquals_1 = (null != _superType_1);
      if (_tripleNotEquals_1) {
        final String qualifiedName_1 = event.getSuperType().getQualifiedName();
        final String package__1 = qualifiedName_1.substring(0, qualifiedName_1.lastIndexOf("."));
        final String depDomain_1 = this.getDomain(package__1, "org.etri.slice.commons", "event");
        boolean _equals_2 = domainFQN.equals(depDomain_1);
        boolean _not_1 = (!_equals_2);
        if (_not_1) {
          this.domainManager.addRef(domain, domainFQN, depDomain_1);
        }
      }
    }
    Iterable<org.etri.slice.tools.adl.domainmodel.Exception> _filter_3 = Iterables.<org.etri.slice.tools.adl.domainmodel.Exception>filter(eContents, org.etri.slice.tools.adl.domainmodel.Exception.class);
    for (final org.etri.slice.tools.adl.domainmodel.Exception exception : _filter_3) {
      JvmParameterizedTypeReference _superType_2 = exception.getSuperType();
      boolean _tripleNotEquals_2 = (null != _superType_2);
      if (_tripleNotEquals_2) {
        final String qualifiedName_2 = exception.getSuperType().getQualifiedName();
        final String package__2 = qualifiedName_2.substring(0, qualifiedName_2.lastIndexOf("."));
        final String depDomain_2 = this.getDomain(package__2, "org.etri.slice.commons", null);
        boolean _equals_3 = domainFQN.equals(depDomain_2);
        boolean _not_2 = (!_equals_3);
        if (_not_2) {
          this.domainManager.addRef(domain, domainFQN, depDomain_2);
        }
      }
    }
    Iterable<AgentDeclaration> _filter_4 = Iterables.<AgentDeclaration>filter(eContents, AgentDeclaration.class);
    for (final AgentDeclaration agent : _filter_4) {
      {
        final Consumer<Behavior> _function_1 = (Behavior behavior) -> {
          final Consumer<JvmTypeReference> _function_2 = (JvmTypeReference type) -> {
            boolean _matched = false;
            if (type instanceof Context) {
              _matched=true;
              final String name = type.getQualifiedName();
              if ((name == "void")) {
                return;
              }
              final String depDomain_3 = name.substring(0, name.lastIndexOf("."));
              boolean _equals_4 = domainFQN.equals(depDomain_3);
              boolean _not_3 = (!_equals_4);
              if (_not_3) {
                this.domainManager.addRef(domain, domainFQN, depDomain_3);
              }
            }
            if (!_matched) {
              if (type instanceof Event) {
                _matched=true;
                final String name = type.getQualifiedName();
                if ((name == "void")) {
                  return;
                }
                final String depDomain_3 = name.substring(0, name.lastIndexOf("."));
                boolean _equals_4 = domainFQN.equals(depDomain_3);
                boolean _not_3 = (!_equals_4);
                if (_not_3) {
                  this.domainManager.addRef(domain, domainFQN, depDomain_3);
                }
              }
            }
          };
          behavior.getSituation().getTypes().forEach(_function_2);
          final Action action = behavior.getAction();
          boolean _matched = false;
          if (action instanceof Publish) {
            _matched=true;
            final String name = ((Publish)action).getEvent().getType().getQualifiedName();
            if ((name == "void")) {
              return;
            }
            final String depDomain_3 = this.getDomain(name, "org.etri.slice.commons", "event");
            boolean _equals_4 = domainFQN.equals(depDomain_3);
            boolean _not_3 = (!_equals_4);
            if (_not_3) {
              this.domainManager.addRef(domain, domainFQN, depDomain_3);
            }
          }
          if (!_matched) {
            if (action instanceof Call) {
              _matched=true;
              final String name = ((Call)action).getControl().getQualifiedName();
              if ((name == "void")) {
                return;
              }
              final String depDomain_3 = this.getDomain(name, "org.etri.slice.commons", "service");
              boolean _equals_4 = domainFQN.equals(depDomain_3);
              boolean _not_3 = (!_equals_4);
              if (_not_3) {
                this.domainManager.addRef(domain, domainFQN, depDomain_3);
              }
            }
          }
        };
        agent.getBehaviorSet().getBehaviors().forEach(_function_1);
        final Consumer<CommandSet> _function_2 = (CommandSet commandSet) -> {
          final Consumer<Command> _function_3 = (Command command) -> {
            final String name = command.getAction().getQualifiedName();
            final String depDomain_3 = this.getDomain(name, "org.etri.slice.commons", "service");
            boolean _equals_4 = domainFQN.equals(depDomain_3);
            boolean _not_3 = (!_equals_4);
            if (_not_3) {
              this.domainManager.addRef(domain, domainFQN, depDomain_3);
            }
          };
          commandSet.getCommands().forEach(_function_3);
        };
        agent.getCommandSets().forEach(_function_2);
      }
    }
    Iterable<DomainDeclaration> _filter_5 = Iterables.<DomainDeclaration>filter(eContents, DomainDeclaration.class);
    for (final DomainDeclaration subDomain : _filter_5) {
      this.traveseDomain(subDomain);
    }
  }
}
