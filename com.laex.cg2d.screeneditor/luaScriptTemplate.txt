ptm = 16 --default
cardNoX = 0
cardNoY = 0
cardWidth = 0
maxXVelocity = 10
onAir = false

screenModel = nil
queryMgr = nil

currentAnimation = "Right"
switchAnimation = false 

destroyBodyList = {}


function init(_screenModel, _screenMgr, world, cam)
	screenModel = _screenModel
	screenMgr = _screenMgr
	
	ptm = _screenModel:getScreenPrefs():getWorldPrefs():getPtmRatio()
	cardNoX = _screenModel:getScreenPrefs():getCardPrefs():getCardNoX()
	cardNoY = _screenModel:getScreenPrefs():getCardPrefs():getCardNoY()
	cardWidth = _screenModel:getScreenPrefs():getCardPrefs():getCardWidth()
end

function initBody(world, cam, body, id)
end

function update(world, cam, body, id)

	if id == "runner" then

		local posX = body:getTransform():getPosition().x
		local posY = body:getTransform():getPosition().y

		local leftBoundsCondition = posX > (cardWidth/ptm/2)
		local rightBoundsCondition = posX < ((cardNoX - 0.5) * cardWidth) / ptm

		if leftBoundsCondition and rightBoundsCondition then
			cam.position.x = posX
		end


		if switchAnimation then
			local newBody = screenMgr:switchAnimation("runner", currentAnimation)
			switchAnimation = false
		end

	end

	if destroyBodyList[id] then
		world:destroyBody(body)
		destroyBodyList[id] = false
	end

end

function doSwitchAnimation(anim)
	if anim ~= currentAnimation then
    	switchAnimation = true
    	currentAnimation = anim
    end
end

function handleRunner(world, cam, key)
	local body = screenMgr:getEntityById("runner")

    if key == "LEFT" then   

		if body:getLinearVelocity().x <= maxXVelocity then
			body:applyLinearImpulse(-1, 0, 0.5, 0)
    	end

    	doSwitchAnimation("Left")
    end

	if key == "RIGHT" then

		if body:getLinearVelocity().x <= maxXVelocity then
			body:applyLinearImpulse(0.8, 0, 0, 0)
		end


		doSwitchAnimation("Right")
	end

	if key == "SPACE" then
		if not onAir then
			body:setLinearVelocity(screenMgr:newVector(body:getLinearVelocity().x, 9))
			onAir = true
		end
	end

end

function keyPressed(world, cam, key)
	handleRunner(world, cam, key)
end

function collisionCallback(idA, idB, bodyA, bodyB, fixA, fixB, world, cam) 
    
    local runnerAndEdge = string.match(idA, "edge") and idB == "runner";

	if  runnerAndEdge  then
		onAir = false
    end

	if idA == "runner" and string.match(idB, "coin") then
		destroyBodyList[idB] = true 
	end

end
