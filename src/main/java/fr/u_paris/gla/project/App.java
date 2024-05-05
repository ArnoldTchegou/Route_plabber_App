package fr.u_paris.gla.project;

import java.io.IOException;

import fr.u_paris.gla.project.App_Ccontroller.Controller;
import fr.u_paris.gla.project.Lecture_Reseau.Network;
import fr.u_paris.gla.project.User_interface.UI_Terminale;
import fr.u_paris.gla.project.User_interface.UI_graphique;
import fr.u_paris.gla.project.User_interface.MetroMapUI;
import fr.u_paris.gla.project.idfm.IDFMNetworkExtractor;

/** Simple application model.
 *
 * @author EIDD_M1_GROUPb */
public class App {

    /** Application entry point.
     *
     * @param args launching arguments 
     * @throws IOException */
    public static void main(String[] args) throws IOException {
        String[] path = {"test.csv"};
        IDFMNetworkExtractor.extract(path);
        if (args.length > 0) {
            for (String string : args) {
                if ("--info".equals(string)) { //$NON-NLS-1$
                    Controller c = new Controller(new Network(), new UI_Terminale());
                    c.App_Info();
                    return;
                }
                if ("--gui".equals(string)) { //$NON-NLS-1$
                    Controller c = new Controller(new Network(), new UI_graphique());
                    c.LaunchApp_GUI();
                }
                if ("--term".equals(string)) { //$NON-NLS-1$
                    Controller c = new Controller(new Network(), new UI_Terminale());
                    c.LaunchApp_Terminale();
                }
                 if ("--map".equals(string)) { //$NON-NLS-1$
                    Controller c = new Controller(new Network());
                    c.LaunchApp_Map();
                }
            }
        }
    }
}



