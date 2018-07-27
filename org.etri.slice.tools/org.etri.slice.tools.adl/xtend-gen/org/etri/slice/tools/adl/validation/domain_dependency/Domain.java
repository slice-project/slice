package org.etri.slice.tools.adl.validation.domain_dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.etri.slice.tools.adl.domainmodel.DomainDeclaration;

@SuppressWarnings("all")
public class Domain {
  public String domain;
  
  public EObject eObject;
  
  public List<Domain> dependencies = new ArrayList<Domain>();
  
  public Domain(final String domain) {
    this.domain = domain;
  }
  
  public Domain(final DomainDeclaration domain, final String domainFQN) {
    this.eObject = domain;
    this.domain = domainFQN;
  }
  
  public boolean addRef(final Domain domain) {
    boolean _xifexpression = false;
    boolean _contains = this.dependencies.contains(domain);
    boolean _not = (!_contains);
    if (_not) {
      _xifexpression = this.dependencies.add(domain);
    }
    return _xifexpression;
  }
  
  public boolean hasCycle() {
    final Stack<String> visited = new Stack<String>();
    return this.hasCycle(this, visited);
  }
  
  public boolean hasCycle(final Domain current, final Stack<String> visited) {
    boolean _isVisited = this.isVisited(visited, current);
    if (_isVisited) {
      return true;
    }
    visited.push(current.domain);
    for (final Domain domain : current.dependencies) {
      boolean _hasCycle = this.hasCycle(domain, visited);
      if (_hasCycle) {
        return true;
      }
    }
    visited.pop();
    return false;
  }
  
  public boolean isVisited(final Stack<String> visited, final Domain current) {
    boolean _xblockexpression = false;
    {
      final Function1<String, Boolean> _function = (String domain) -> {
        return Boolean.valueOf(current.domain.equals(domain));
      };
      final Iterable<String> exists = IterableExtensions.<String>filter(visited, _function);
      int _size = IterableExtensions.size(exists);
      _xblockexpression = (_size > 0);
    }
    return _xblockexpression;
  }
  
  public boolean hasDependencies() {
    int _size = this.dependencies.size();
    return (_size != 0);
  }
}
