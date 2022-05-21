/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

/**
 *
 * @author marta
 */
public class Auxiliar {
    
    private int aciertos;
    private int fallos;
    
    public Auxiliar(int aciertos, int fallos) {this.aciertos = aciertos; this.fallos = fallos;}
    
    public int getAciertos() {return aciertos;}
    public void setAciertos(int aciertos) {this.aciertos = aciertos;}
    
    public int getFallos() {return fallos;}
    public void setFallos(int fallos) {this.fallos = fallos;}
}
