package me.tryfle.stormy.events;

import net.weavemc.loader.api.event.Event;

public class StrafeEvent extends Event {
    public StrafeEvent(float forward, float strafe, float friction, float attributeSpeed, float yaw) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
        this.attributeSpeed = attributeSpeed;
        this.yaw = yaw;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setAttributeSpeed(float attributeSpeed) {
        this.attributeSpeed = attributeSpeed;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getForward() {
        return forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public float getFriction() {
        return friction;
    }

    public float getAttributeSpeed() {
        return attributeSpeed;
    }

    public float getYaw() {
        return yaw;
    }

    private float forward, strafe;
    private float friction, attributeSpeed;
    private float yaw;

}