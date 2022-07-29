package myObjectAnalyzer;

import equals.Employee;

import java.lang.reflect.*;


public class Analyzer {
    public static final int FIELDS=1;
    public static final int METHODS=2;
    private String result="";
    public String toString(Object obj,int type){
        if(type==1){
            result=printFields(obj);
        }else if(type==2){
            result=printMethods(obj);
        }
        return result;
    }
    private String printMethods(Object obj){
        if(obj==null)return "Object is null";
        Class cl=obj.getClass();
        Method[]methods=cl.getDeclaredMethods();
        result+=cl.getName()+"{\n";
        for(Method me:methods){
            String parameters="";
            for(int i=0;i<me.getParameterTypes().length;i++){
                parameters+=me.getParameterTypes()[i].getName();
                if(i!=me.getParameterTypes().length-1)parameters+=",";
            }
            result+=Modifier.toString(me.getModifiers())+" "+me.getReturnType()+" "+me.getName()+"("+parameters+")"+"\n";
        }
        return result;
    }
    private String printFields(Object obj) {
        if(obj==null)return null;
        Class cl=obj.getClass();
        if(cl.getTypeName().equals("java.lang.String"))return cl.getTypeName()+"->"+obj;
        if(cl.isArray()){
            int length= Array.getLength(obj);
            result+=cl.getTypeName()+"->{\n";
            if(cl.getComponentType()==String.class||cl.getComponentType().isPrimitive()){
            for(int i=0;i<length;i++){
                if(i!=length-1)
             result+=cl.getComponentType()+"->"+Array.get(obj,i)+",";
                else result+=cl.getComponentType()+"->"+Array.get(obj,i);
            }
            }else{
                for(int i=0;i<length;i++){
                    Object val = Array.get(obj, i);
                    if(val!=null){
                    printFields(val);
                    result+=" ";}
                }
            }
            result+="}";
        }
        Field[]fields=cl.getDeclaredFields();
        if(fields.length!=0) result+=cl.getTypeName()+"=<{\n";
        AccessibleObject.setAccessible(fields,true);
        for(Field f:fields){
            Class t = f.getType();
            if(!Modifier.isStatic(f.getModifiers())){
                result+=f.getName()+"=";
                if(t.isPrimitive()||t==String.class) {
                    try {
                        result+=f.get(obj)+"\n";
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    try {
                        printFields(f.get(obj));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if(fields.length!=0)result+=">}\n";
        return result;
    }

}
