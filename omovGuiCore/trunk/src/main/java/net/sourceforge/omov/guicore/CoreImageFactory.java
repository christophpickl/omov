package net.sourceforge.omov.guicore;


public class CoreImageFactory extends AbstractImageFactory {

    private static final CoreImageFactory INSTANCE = new CoreImageFactory();

    public static CoreImageFactory getInstance() {
        return INSTANCE;
    }
    
    // somehow this class got empty :)
}
