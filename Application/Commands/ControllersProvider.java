package com.company.Application.Commands;


import com.company.Application.Controllers.DbControllerInterface;
import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Controllers.TreeMapController;


/**
 *  produce access to controllers
 */
public class ControllersProvider {

    private final TreeMapController treeMapController;
    private final DbControllerInterface dbController;


    public ControllersProvider(TreeMapController treeMapController, DbControllerInterface dbController){

        this.treeMapController = treeMapController;
        this.dbController = dbController;
    }


    /**
     * produce access to TreeMapController
     * @return treeMapController
     */
    public TreeMapController getTreeMapController() {
        return treeMapController;
    }

    /**
     * produce access to XMLController
     * @return xmlController
     */
    public DbControllerInterface getDbController() {
        return dbController;
    }

}
