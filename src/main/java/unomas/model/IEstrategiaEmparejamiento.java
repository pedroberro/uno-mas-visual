package unomas.model;

import java.util.List;

public interface IEstrategiaEmparejamiento {
    List<Usuario> emparejar(List<Usuario> candidatos);

    String nombre();
}
