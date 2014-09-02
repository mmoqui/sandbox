package org.silverpeas.jcr.security;

import org.apache.jackrabbit.core.id.ItemId;
import org.apache.jackrabbit.core.security.AMContext;
import org.apache.jackrabbit.core.security.AccessManager;
import org.apache.jackrabbit.core.security.authorization.AccessControlProvider;
import org.apache.jackrabbit.core.security.authorization.WorkspaceAccessManager;
import org.apache.jackrabbit.spi.Name;
import org.apache.jackrabbit.spi.Path;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;

/**
 * @author mmoquillon
 */
public class JcrAccessManager implements AccessManager {
  @Override
  public void init(final AMContext amContext) throws AccessDeniedException, Exception {

  }

  @Override
  public void init(final AMContext amContext, final AccessControlProvider accessControlProvider,
      final WorkspaceAccessManager workspaceAccessManager) throws AccessDeniedException, Exception {

  }

  @Override
  public void close() throws Exception {

  }

  @Override
  public void checkPermission(final ItemId itemId, final int i)
      throws AccessDeniedException, ItemNotFoundException, RepositoryException {

  }

  @Override
  public void checkPermission(final Path path, final int i)
      throws AccessDeniedException, RepositoryException {

  }

  @Override
  public void checkRepositoryPermission(final int i)
      throws AccessDeniedException, RepositoryException {

  }

  @Override
  public boolean isGranted(final ItemId itemId, final int i)
      throws ItemNotFoundException, RepositoryException {
    return true;
  }

  @Override
  public boolean isGranted(final Path path, final int i) throws RepositoryException {
    return true;
  }

  @Override
  public boolean isGranted(final Path path, final Name name, final int i)
      throws RepositoryException {
    return true;
  }

  @Override
  public boolean canRead(final Path path, final ItemId itemId) throws RepositoryException {
    return true;
  }

  @Override
  public boolean canAccess(final String s) throws RepositoryException {
    return true;
  }
}
