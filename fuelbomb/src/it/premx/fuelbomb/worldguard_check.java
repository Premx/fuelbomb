/*
 * Copyright (C) 2015 Premx <premx at web.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package it.premx.fuelbomb;

/**
 *
 * @author Premx
 */
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class worldguard_check {

    public static boolean checkPerms(Player p, Location loc) {

        if (fuelbomb.wg.canBuild(p, loc)) {
            //do something if player can build
            return true;
        } else {

            //p.sendMessage("Nein, WG sagt nej");
            //do something else if player can't build
            return false;
        }
    }

}
