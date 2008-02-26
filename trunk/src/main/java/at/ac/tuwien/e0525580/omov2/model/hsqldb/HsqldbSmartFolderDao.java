package at.ac.tuwien.e0525580.omov2.model.hsqldb;


//public class HsqldbSmartFolderDao extends AbstractHsqldbDao implements ISmartFolderDao {
public class HsqldbSmartFolderDao {
    /*
    private final PreparedStatement selectAllSmartFolders;
    
    public HsqldbSmartFolderDao(HsqldbConnection connection) {
        super(connection);
        
        try {
            
            this.createTableIfNecessary("smartfolder", "CREATE_SMARTFOLDER");

            this.selectAllSmartFolders = this.prepareStatement(this.getSql("SELECT_ALL_SMARTFOLDERS"));
            
        } catch (SQLException e) {
            throw new RuntimeException(e); //  be nicer
        }
    }

    public List<SmartFolder> getAllSmartFolders() throws BusinessException {

            try {
                final List<SmartFolder> result = new LinkedList<SmartFolder>();
                final ResultSet rs = this.selectAllSmartFolders.executeQuery();
                while(rs.next()) {
                    final int id = rs.getInt(1);
                    final String title = rs.getString(2);
                    result.add(new SmartFolder(id, title, null));
                }
                
                return result;
            } catch (SQLException e) {
                throw new BusinessException("Could not insert movie", e);
            }
        }

    @Override
    void notifyListeners() {
        //  Auto-generated method stub
        
    }

    public SmartFolder insertSmartFolder(SmartFolder smartFolder) throws BusinessException {
        //  Auto-generated method stub
        return null;
    }

    public void registerMovieDaoListener(ISmartFolderDaoListener listener) {
        //  Auto-generated method stub
        
    }

    public void unregisterMovieDaoListener(ISmartFolderDaoListener listener) {
        //  Auto-generated method stub
        
    }

    public void deleteSmartFolder(SmartFolder smartFolder) throws BusinessException {
        //  Auto-generated method stub
        
    }
    */
}
