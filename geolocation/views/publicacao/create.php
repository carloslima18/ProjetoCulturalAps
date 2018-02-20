<?php

use yii\helpers\Html;


/* @var $this yii\web\View */
/* @var $model app\models\Publicacao */

$this->title = 'Create Publicação';
$this->params['breadcrumbs'][] = ['label' => 'Publicação', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacao-create">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
