package org.etri.slice.tools.adl.validation.domain_dependency;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;
import org.etri.slice.tools.adl.validation.domain_dependency.Domain;

@Singleton
@SuppressWarnings("all")
public class DomainManager {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  public HashMap<String, Domain> domains = new HashMap<String, Domain>();
  
  public void init() {
    this.domains.clear();
  }
  
  public boolean existCycle() {
    boolean _xblockexpression = false;
    {
      Collection<Domain> _values = this.domains.values();
      for (final Domain domain : _values) {
        boolean _hasCycle = domain.hasCycle();
        if (_hasCycle) {
          return true;
        }
      }
      _xblockexpression = false;
    }
    return _xblockexpression;
  }
  
  public Domain addDomain(final DomainDeclaration domain) {
    Domain _xblockexpression = null;
    {
      final String domainFQN = this._iQualifiedNameProvider.getFullyQualifiedName(domain).toString();
      Domain _xifexpression = null;
      boolean _containsKey = this.domains.containsKey(domain);
      boolean _not = (!_containsKey);
      if (_not) {
        Domain _domain = new Domain(domain, domainFQN);
        _xifexpression = this.domains.put(domainFQN, _domain);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public boolean addRef(final DomainDeclaration domain, final String source, final String target) {
    boolean _xblockexpression = false;
    {
      boolean _containsKey = this.domains.containsKey(source);
      boolean _not = (!_containsKey);
      if (_not) {
        Domain _domain = new Domain(domain, source);
        this.domains.put(source, _domain);
      }
      boolean _containsKey_1 = this.domains.containsKey(target);
      boolean _not_1 = (!_containsKey_1);
      if (_not_1) {
        Domain _domain_1 = new Domain(target);
        this.domains.put(target, _domain_1);
      }
      _xblockexpression = this.domains.get(source).addRef(this.domains.get(target));
    }
    return _xblockexpression;
  }
  
  public ArrayList<Domain> getBuildOrderedDomainList() {
    ArrayList<Domain> _xblockexpression = null;
    {
      final ArrayList<Domain> orderedList = new ArrayList<Domain>();
      final Consumer<Domain> _function = (Domain domain) -> {
        int _indexOf = orderedList.indexOf(domain);
        boolean _equals = ((-1) == _indexOf);
        if (_equals) {
          orderedList.add(domain);
        }
        final int index = orderedList.indexOf(domain);
        boolean _hasDependencies = domain.hasDependencies();
        if (_hasDependencies) {
          final Procedure2<Domain, Integer> _function_1 = (Domain dep, Integer index2) -> {
            final int depIndex = orderedList.indexOf(dep);
            if (((-1) == depIndex)) {
              orderedList.add(index, dep);
            } else {
              if ((index < depIndex)) {
                orderedList.remove(index);
                orderedList.remove(dep);
                orderedList.add(index, dep);
                orderedList.add(depIndex, domain);
              }
            }
          };
          IterableExtensions.<Domain>forEach(domain.dependencies, _function_1);
        }
      };
      this.domains.values().forEach(_function);
      _xblockexpression = orderedList;
    }
    return _xblockexpression;
  }
  
  public Domain getDomain(final String domain) {
    return this.domains.get(domain);
  }
}
