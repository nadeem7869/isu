/*
 * Copyright (C) 2015-2016 Willi Ye <williye97@gmail.com>
 *
 * This file is part of Kernel Adiutor.
 *
 * Kernel Adiutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Adiutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Adiutor.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.bhb27.isu.tools;

import android.util.Log;

import com.bhb27.isu.tools.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 08.02.15.
 */

/**
 * This class is kinda similar to {@link java.io.File}.
 * Only difference is that this class runs as Root.
 */
public class RootFile {

    private final String file;

    public RootFile(String file) {
        this.file = file;
    }

    public String getName() {
        return RootUtils.runCommand("basename '" + file + "'");
    }

    public void mkdir() {
        RootUtils.runCommand("mkdir -p -m777 '" + file + "'");
    }

    public void mv(String newPath) {
        RootUtils.runCommand("mv -f '" + file + "' '" + newPath + "'");
    }

    public void write(String text, boolean append) {
        String[] textarray = text.split("\\r?\\n");
        RootUtils.runCommand(append ? "echo '" + textarray[0] + "' >> " + file : "echo '" + textarray[0] + "' > " + file);
        if (textarray.length > 1)
            for (int i = 1; i < textarray.length; i++)
                RootUtils.runCommand("echo '" + textarray[i] + "' >> " + file);
    }

    public void Iwrite(String text, boolean append) {
        String[] textarray = text.split("\\r?\\n");
        RootUtils.runICommand(append ? "echo '" + textarray[0] + "' >> " + file : "echo '" + textarray[0] + "' > " + file);
        if (textarray.length > 1)
            for (int i = 1; i < textarray.length; i++)
                RootUtils.runICommand("echo '" + textarray[i] + "' >> " + file);
    }

    public void delete() {
        RootUtils.runCommand("rm -r '" + file + "'");
    }

    public List < String > list() {
        List < String > list = new ArrayList < > ();
        String files = RootUtils.runCommand("ls '" + file + "'");
        if (files != null)
        // Make sure the file exists
            for (String file: files.split("\\r?\\n"))
            if (file != null && !file.isEmpty() && Tools.existFile(this.file + "/" + file, true))
                list.add(file);
        return list;
    }

    public List < RootFile > listFiles() {
        List < RootFile > list = new ArrayList < > ();
        String files = RootUtils.runCommand("ls '" + file + "'");
        if (files != null)
        // Make sure the file exists
            for (String file: files.split("\\r?\\n"))
            if (file != null && !file.isEmpty() && Tools.existFile(this.file + "/" + file, true))
                list.add(new RootFile(this.file + "/" + file));
        return list;
    }

    public float length() {
        try {
            return Float.parseFloat(RootUtils.runCommand("du '" + file + "'").split(file)[0].trim());
        } catch (Exception ignored) {
            return 0;
        }
    }

    public String getParent() {
        return RootUtils.runCommand("dirname '" + file + "'");
    }

    public boolean isEmpty() {
        return RootUtils.runCommand("find '" + file + "' -mindepth 1 | read || echo false").equals("false");
    }

    public boolean exists() {
        String output = RootUtils.runCommand("[ -e '" + file + "' ] && echo true");
        return output != null && output.contains("true");
    }

    public boolean Iexists() {
        String output = RootUtils.runICommand("[ -e '" + file + "' ] && echo true");
        return output != null && output.contains("true");
    }

    public String readFile() {
        return RootUtils.runCommand("cat '" + file + "'");
    }

    public String IreadFile() {
        return RootUtils.runICommand("cat '" + file + "'");
    }

    public String toString() {
        return file;
    }

}
