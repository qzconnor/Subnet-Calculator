package de.sc.models;

import de.sc.utils.SubnetUtils;

public class CalculationModel {

    public SubnetUtils.SubnetInfo calculate(String address, String CIDR){
        //Create Object of SubnetUtils and combine address with cidr
        // like 10.10.10.10/24
        SubnetUtils utils = new SubnetUtils(address + "/" + CIDR);
        //Return the calculated information.
        return utils.getInfo();
    }
}
